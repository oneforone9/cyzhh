package com.essence.job.backjob.order;

import com.alibaba.fastjson.JSON;
import com.essence.entity.MessageBean;
import com.essence.interfaces.api.WorkorderProcessService;
import com.essence.interfaces.model.WorkorderNewestEsr;
import com.essence.interfaces.model.WorkorderTrackEsr;
import com.essence.job.validate.CronJobIdentifierProvider;
import com.essence.service.OrderTrackWebSocketServer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_END;

/**
 * 推送工单轨迹定时任务
 *
 * @author mjj
 * @since 2022/11/11 12:07
 */
@Component
@Log4j2
public class WorkWebSocketTask {
    @Autowired
    private WorkorderProcessService workorderProcessService;

    @Autowired
    private OrderTrackWebSocketServer orderTrackWebSocketServer;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Scheduled(fixedRate = 1000)
    public void getWorkWebSocketTask() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-工单轨迹信息推送任务,结束.", BACK_END);
            return;
        }
        try {
            log.info("工单轨迹信息推送开始getWorkWebSocketTask{}");
            //查询工单
            List<WorkorderNewestEsr> workorderNewestEsrList = workorderProcessService.selectWorkorderProcessList();
            if (null != workorderNewestEsrList && workorderNewestEsrList.size() > 0) {
                List<String> collect = workorderNewestEsrList.stream().map(x -> x.getId()).collect(Collectors.toList());
                List<WorkorderTrackEsr> workorderTrackEsrList = workorderProcessService.selectWorkorderProcessTrack(collect);
                if (null != workorderTrackEsrList && workorderTrackEsrList.size() > 0) {
                    MessageBean messageBean = new MessageBean();
                    messageBean.setCode(1);
                    String text = JSON.toJSONString(workorderTrackEsrList);
                    messageBean.setContent(text);
                    String text1 = JSON.toJSONString(messageBean);
                    orderTrackWebSocketServer.onMessage(text1);
                    pushWebSocket(workorderTrackEsrList);
                }
            }
            log.info("工单轨迹信息推送结束getWorkWebSocketTask{}");
        } catch (Exception e) {
            log.info("工单轨迹信息推送异常getWorkWebSocketTask{}" + e);
        }
    }

    private void pushWebSocket(List<WorkorderTrackEsr> workorderTrackEsrList) {
        for (WorkorderTrackEsr workorderTrackEsr : workorderTrackEsrList) {
            MessageBean messageBean = new MessageBean();
            messageBean.setCode(2);
            String text = JSON.toJSONString(workorderTrackEsr);
            messageBean.setContent(text);
            String text1 = JSON.toJSONString(messageBean);
            orderTrackWebSocketServer.onMessage(text1);
        }

    }
}
