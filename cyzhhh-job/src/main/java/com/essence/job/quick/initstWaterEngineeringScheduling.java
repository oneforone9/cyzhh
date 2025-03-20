package com.essence.job.quick;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.cache.service.RedisService;
import com.essence.dao.StCaseBaseInfoDao;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import com.essence.interfaces.api.StWaterEngineeringSchedulingService;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 取消缓存采用 算方案时候添加
 * 针对慢查询, 采取存储redis缓存方案  /stWaterEngineeringScheduling/selectList?caseId=7e16aa37fc714e8b878878e703d7b987
 *
 * @Author cuirx
 * @Description TODO
 * @Date 2023/7/17 16:24
 */
@Component
@Slf4j
public class initstWaterEngineeringScheduling {

    /**
     * 注入redis
     */
    @Autowired
    RedisService redisService;

    /**
     * 要提速的方法
     */
    @Autowired
    private StWaterEngineeringSchedulingService stWaterEngineeringSchedulingService;

    @Resource
    private StCaseBaseInfoDao caseBaseInfoDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 0/50 * * * ?")
    public void sync() {
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

    public void initWaterTransit() {
        List<StCaseBaseInfoDto> stCaseBaseInfoDtos = caseBaseInfoDao.selectList(new QueryWrapper<>());
        List<String> collect = stCaseBaseInfoDtos.parallelStream().map(StCaseBaseInfoDto::getId).collect(Collectors.toList());
        for (String s : collect) {
            List<StWaterEngineeringSchedulingDto> stWaterEngineeringSchedulingDtos = stWaterEngineeringSchedulingService.selectEngineeringScheduling(s);
            Object cacheObject = redisService.getCacheObject("quick:" + s);
            if (cacheObject != null) {
                continue;
            }
            redisService.setCacheObject("quick:" + s, stWaterEngineeringSchedulingDtos);
        }
    }

}
