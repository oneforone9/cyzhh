package com.essence.job.quick;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.FileCacheUtils;
import com.essence.dao.ReaBaseDao;
import com.essence.dao.StCaseBaseInfoDao;
import com.essence.dao.entity.ReaBase;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.interfaces.api.StCaseResService;
import com.essence.interfaces.dot.RiverStepSectionDto;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 实际水深断面 ssd 定时任务缓存
 * key "quick:fact:"+方案id
 */
@Component
@Slf4j
public class QuickFactSectionWaterSSDCacheHandel {
    @Resource
    private StCaseBaseInfoDao caseBaseInfoDao;

    @Autowired
    private StCaseResService stCaseResService;
    @Resource
    private ReaBaseDao reaBaseDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Scheduled(cron = "0 0 23 * * ?")
    public void sync() throws ParseException, ExecutionException, InterruptedException {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initWaterTransit任务,结束.", BACK_JOB);
            return;
        }
        log.info("initWaterTransit任务开始执行...");
        initWaterTransit();
        log.info("initWaterTransit任务执行结束.");
    }

    public void initWaterTransit() throws ParseException, ExecutionException, InterruptedException {
        List<StCaseBaseInfoDto> stCaseBaseInfoDtos = caseBaseInfoDao.selectList(new QueryWrapper<>());
        List<String> collect = stCaseBaseInfoDtos.parallelStream().map(StCaseBaseInfoDto::getId).collect(Collectors.toList());

        List<String> reaId = reaBaseDao.selectList(new QueryWrapper<>()).parallelStream().filter(reaBase -> {
            return StrUtil.isNotBlank(reaBase.getReaName());
        }).map(ReaBase::getId).collect(Collectors.toList());
        for (String s : collect) {
            for (String string : reaId) {

                StCaseBaseInfoEsu stCaseBaseInfoEsu = new StCaseBaseInfoEsu();
                stCaseBaseInfoEsu.setRiverId(string);
                stCaseBaseInfoEsu.setId(s);
                //如果这个文件存在则跳过
                if (FileUtil.exist(FileCacheUtils.Dir + File.pathSeparator + "quick_fact" + stCaseBaseInfoEsu.getId() + "_" + stCaseBaseInfoEsu.getRiverId() + ".txt")) {
                    continue;
                }
                List<RiverStepSectionDto> riverFactSection = stCaseResService.getRiverFactSection(stCaseBaseInfoEsu);
                FileCacheUtils.set("quick_fact" + stCaseBaseInfoEsu.getId() + "_" + stCaseBaseInfoEsu.getRiverId(), JSON.toJSONString(riverFactSection));
            }

        }

    }

}
