package com.essence.job.ruler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.Gzzx8717JcJsjcxxbDao;
import com.essence.dao.SyncWaterRulerDao;
import com.essence.dao.entity.Gzzx8717JcJsjcxxbDto;
import com.essence.dao.entity.SyncWaterRulerDto;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 同步电子水尺数据
 *
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/23 10:53
 */
@Component
@Slf4j
public class SyncWaterRulerHandler {

    @Autowired
    Gzzx8717JcJsjcxxbDao gzzx8717JcJsjcxxbDao;
    @Autowired
    SyncWaterRulerDao syncWaterRulerDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 同步电子水尺数据 市局 2分钟开始频率为5分钟, 增量推送, 每次推送1分钟内
     */
    @Scheduled(cron = "0 * * * * ?")
    public void execute() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行同步电子水尺数据任务,结束.", BACK_JOB);
            return;
        }
        log.info("同步电子水尺数据开始...");
        Date latest = doExecuteJudge();
        log.info("同步电子水尺数据,最近同步数据时间为:" + latest);
        List<Gzzx8717JcJsjcxxbDto> rulerData = doExecuteGet(latest);
        log.info("电子水尺数据获取到{}条数据,准备保存...", rulerData.size());
        if (rulerData.size() > 0) {
            doExecuteSave(rulerData);
            log.info("电子水尺数据保存完毕.");
        }
    }

    /**
     * 获取最近一条数据同步时间
     *
     * @return
     */
    public Date doExecuteJudge() {
        QueryWrapper<SyncWaterRulerDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rksj");
        List<SyncWaterRulerDto> syncWaterRulerDtos = syncWaterRulerDao.selectList(queryWrapper);
        if (syncWaterRulerDtos.size() > 0) {
            return syncWaterRulerDtos.get(0).getRksj();
        } else {
            return null;
        }
    }

    /**
     * 获取电子水尺数据
     */
    @SneakyThrows
    private List<Gzzx8717JcJsjcxxbDto> doExecuteGet(Date latest) {
        QueryWrapper<Gzzx8717JcJsjcxxbDto> queryWrapper = new QueryWrapper<>();
        if (latest != null) {
            queryWrapper.gt("rksj", latest);
        }
        List<Gzzx8717JcJsjcxxbDto> resList = gzzx8717JcJsjcxxbDao.selectList(queryWrapper);
        return resList;
    }

    /**
     * 存储电子水尺数据
     */
    @SneakyThrows
    private void doExecuteSave(List<Gzzx8717JcJsjcxxbDto> rulerData) {
        ArrayList<SyncWaterRulerDto> list = new ArrayList<>();
        for (Gzzx8717JcJsjcxxbDto rulerDatum : rulerData) {
            SyncWaterRulerDto rulerDto = new SyncWaterRulerDto();
            rulerDto.setId(String.valueOf(UUID.randomUUID()).replaceAll("-", ""));
            rulerDto.setWaterCode(rulerDatum.getCzdm());
            rulerDto.setTime(rulerDatum.getSj());
            rulerDto.setAmount(rulerDatum.getJs());
            rulerDto.setSwm(rulerDatum.getSwm());
            rulerDto.setRksj(rulerDatum.getRksj());
            rulerDto.setCreateTime(new Date());
            list.add(rulerDto);
        }
        syncWaterRulerDao.insertAll(list);
    }
}
