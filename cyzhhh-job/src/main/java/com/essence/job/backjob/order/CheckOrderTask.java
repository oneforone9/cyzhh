package com.essence.job.backjob.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.WorkorderBaseDao;
import com.essence.dao.entity.WorkorderBase;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_END;

/**
 * 用于检查
 */
@Component
@Log4j2
public class CheckOrderTask {
    @Resource
    private OrderTask orderTask;
    @Autowired
    private WorkorderBaseDao workorderBaseDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Scheduled(cron = "0 0/1 7 * * ?")
    public void check() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-检查是否生成工单任务,结束.", BACK_END);
            return;
        }
        log.info("开始检查是否生成工单");
        FileUtil.writeString("檢測中", new File("checl.txt"), "UTF-8");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.le("start_time", DateUtil.endOfDay(new Date()));
        wrapper.ge("start_time", DateUtil.beginOfDay(new Date()));
        List<WorkorderBase> workorderBases = workorderBaseDao.selectList(wrapper);
        if (CollUtil.isEmpty(workorderBases)) {
            log.info("今天未能生成工单，检查开始执行工单");
            orderTask.syncCreateOrderTask();
        } else {
            log.info("今天已经生成工单，检查不需要执行工单");
        }
    }
}
