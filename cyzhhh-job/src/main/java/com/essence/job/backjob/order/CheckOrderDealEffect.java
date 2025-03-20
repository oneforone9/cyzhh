package com.essence.job.backjob.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.WorkorderBaseDao;
import com.essence.dao.WorkorderNewestDao;
import com.essence.dao.WorkorderProcessDao;
import com.essence.dao.entity.WorkorderBase;
import com.essence.dao.entity.WorkorderNewest;
import com.essence.dao.entity.WorkorderProcess;
import com.essence.interfaces.api.WorkorderBaseService;
import com.essence.interfaces.model.WorkorderBaseEsu;
import com.essence.interfaces.model.WorkorderNewestEsr;
import com.essence.job.validate.CronJobIdentifierProvider;
import com.essence.service.converter.ConverterWorkorderNewestTtoR;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_END;

/**
 * @Author: liwy
 * @CreateTime: 2023-06-15  15:24
 */
@Component
@Log4j2
public class CheckOrderDealEffect {
    @Resource
    private WorkorderBaseService workorderBaseService;

    @Resource
    private WorkorderProcessDao workorderProcessDao;

    @Resource
    private ConverterWorkorderNewestTtoR converterWorkorderNewestTtoR;

    @Autowired
    private WorkorderNewestDao workorderNewestDao;

    @Autowired
    private WorkorderBaseDao workorderBaseDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Scheduled(cron = "0 30 1 * * ?")
    public void check() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-处理工单的处理时效任务,结束.", BACK_END);
            return;
        }
        log.info("开始检查处理工单的处理时效 —— 只处理三方功能的-----------开始");
        QueryWrapper wrapper = new QueryWrapper();
        //结束时间是昨天的单子
        wrapper.le("end_time", DateUtil.beginOfDay(new Date()));
        wrapper.in("order_type", ItemConstant.ORDER_TYPE_BJ, ItemConstant.ORDER_TYPE_LH, ItemConstant.ORDER_TYPE_WB, ItemConstant.ORDER_TYPE_YX);
        wrapper.in("order_status", ItemConstant.ORDER_STATUS_NO_START, ItemConstant.ORDER_STATUS_RUNNING);
        wrapper.last(" and (deal_effect is null or deal_effect ='') ");
        //（1未派发 2未开始 3进行中 4待审核 5已归档 6已终止）
        List<WorkorderNewest> WorkorderNewestEsr = workorderNewestDao.selectList(wrapper);
        log.info("三方工单数：" + WorkorderNewestEsr.size());

        //4巡查完后完成工单进入待审核时，判断完成的时间和截止时间，判断处理时效 ：逾期 还是 按期
        Date dateNow = new Date();
        for (int i = 0; i < WorkorderNewestEsr.size(); i++) {
            WorkorderNewest workorderNewest = WorkorderNewestEsr.get(i);
            Date endTime = workorderNewest.getEndTime();

            WorkorderBase workorderBase = new WorkorderBase();
            //int res = date1.compareTo(date2)，相等则返回0，date1大返回1，否则返回-1。
            //当前系统时间完成工单的时间 大于等于 截止时间
            if (dateNow.compareTo(endTime) > 0) {
                //逾期
                workorderBase.setDealEffect("逾期");
            } else {
                //按期
                workorderBase.setDealEffect("按期");
            }
            workorderBase.setId(workorderNewest.getId());
            workorderBaseDao.updateById(workorderBase);
            //更新公单的处理时效
        }
        log.info("开始检查处理工单的处理时效 —— 只处理三方功能的-----------结束");

    }

    /**
     * 处理驳回工单的 状态
     * 处理逻辑：工单 fixTime rejectTime 不为空则为驳回工单 这两个时间相剪 得出逾期整改 或者 按期整改
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void dealRejectOrder() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-处理驳回工单的状态任务,结束.", BACK_END);
            return;
        }
        log.info("处理驳回工单的状态,开始...");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.isNotNull("reject_time");
        List<WorkorderNewest> workorderBases = workorderNewestDao.selectList(queryWrapper);
        List<WorkorderNewestEsr> list = converterWorkorderNewestTtoR.toList(workorderBases);
        for (WorkorderNewestEsr item : list) {
            //查看该工单的过程表是否有过程拥有 整改时间 如果有的话 则表示是 整改工单
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("order_id", item.getId());
            List<WorkorderProcess> workorderProcess = workorderProcessDao.selectList(wrapper);
            if (CollUtil.isNotEmpty(workorderProcess)) {
                List<WorkorderProcess> collect = workorderProcess.parallelStream().filter(workorderProcess1 -> {
                    return workorderProcess1.getFixTime() != null;
                }).sorted(Comparator.comparing(WorkorderProcess::getOrderTime).reversed()).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(collect)) {
                    //不为空表示拥有
                    item.setRefused(true);
                    item.setFixTime(collect.get(0).getFixTime());
                }
            }
        }

        if (CollUtil.isNotEmpty(list)) {
            List<WorkorderNewestEsr> collect = list.parallelStream().filter(workorderNewestEsr -> {
                return null != workorderNewestEsr.getFixTime() && null != workorderNewestEsr.getRejectTime();
            }).collect(Collectors.toList());
            // 判断整改 逾期 或者 按期
            for (WorkorderNewestEsr p : collect) {
                //完成时间
                Date rejectTime = p.getRejectTime();
                //整改截止时间
                Date fixTime = p.getFixTime();
                if (null != rejectTime && null != fixTime) {
                    int i = rejectTime.compareTo(fixTime);
                    if (i > 0) {
                        String distanceDateTime = TimeUtil.getDistanceDateTime(fixTime, rejectTime);
                        p.setExpireFixTime("逾期：" + distanceDateTime);
                        WorkorderBaseEsu workorderBaseEsu = new WorkorderBaseEsu();
                        workorderBaseEsu.setId(p.getId());
                        workorderBaseEsu.setRejectEffect("逾期整改");
                        workorderBaseService.update(workorderBaseEsu);
                    } else {
                        String distanceDateTime = TimeUtil.getDistanceDateTime(rejectTime, fixTime);
                        p.setExpireFixTime("按期：" + distanceDateTime);
                        WorkorderBaseEsu workorderBaseEsu = new WorkorderBaseEsu();
                        workorderBaseEsu.setId(p.getId());
                        workorderBaseEsu.setRejectEffect("按期整改");
                        workorderBaseService.update(workorderBaseEsu);
                    }
                }
            }
        }
        log.info("处理驳回工单的状态,结束.");
    }
}
