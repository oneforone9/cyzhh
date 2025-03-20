package com.essence.service;

import com.alibaba.fastjson.JSONObject;
import com.essence.entity.MessageBean;
import com.essence.interfaces.api.WorkorderTrackService;
import com.essence.interfaces.model.WorkorderTrackEsr;
import com.essence.interfaces.model.WorkorderTrackEsu;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhy
 * @since 2022/9/21 15:55
 */
@ServerEndpoint("/webSocket/{type}/{orderId}")
@Component
@Log4j2
public class OrderTrackWebSocketServer {

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        OrderTrackWebSocketServer.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("type") String type, @PathParam("orderId") String orderId) {
        log.info("建立连接 orderId:{}", orderId);

        sessionPools.put(type + "_" + orderId, session);



    }

    @OnClose
    public void onClose(@PathParam("type") String type, @PathParam("orderId") String orderId) {
        sessionPools.remove(type + "_" + orderId);
    }

    @OnMessage
    public void onMessage(String message) {
        MessageBean messageBean = JSONObject.parseObject(message, MessageBean.class);
        // 系统接受
        if (0 == messageBean.getCode()){
            toDB(messageBean.getContent());
            return;
        }
        // 发pc
        if (1 == messageBean.getCode()){
            toPC(messageBean.getContent());
            return;
        }

            toXCX(messageBean.getContent());
    }

    @OnError
    public void onError(Throwable throwable) {
        log.info("连接错误");
        throwable.printStackTrace();
    }

    // 发送工单消息
    private void toPC(String message) {
        log.info("发送给PC");
        sessionPools.entrySet().forEach(
                p->{
                    if (!p.getKey().startsWith("PC_")){
                        return;
                    }
                    // 发送消息
                    try {
                        sendMessage(p.getValue(), message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

    }

    // 发送工单消息
    private void toXCX(String message) {
        log.info("发送给小程序");
        WorkorderTrackEsr workorderTrackEsr = JSONObject.parseObject(message, WorkorderTrackEsr.class);
        String orderId = workorderTrackEsr.getOrderId();
        Session session = sessionPools.get("XCX_" + orderId);
        try {
            sendMessage(session, message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 接受消息入库
    private void toDB(String message) {
        log.info("入库");
        WorkorderTrackService workorderTrackService = applicationContext.getBean(WorkorderTrackService.class);
        WorkorderTrackEsu workorderTrackEsu = JSONObject.parseObject(message, WorkorderTrackEsu.class);
        workorderTrackService.insert(workorderTrackEsu);

    }

    // 发送消息
    private void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        }
    }


}
