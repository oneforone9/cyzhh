package com.essence;

import com.alibaba.fastjson.JSON;
import com.essence.service.OrderTrackWebSocketServer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zhy
 * @since 2022/1/10 13:38
 */
@SpringBootApplication
@Log4j2
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.essence")
@EnableAsync
public class CyzhhhApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CyzhhhApplication.class, args);
        OrderTrackWebSocketServer.setApplicationContext(applicationContext);
        log.info("朝阳智慧河湖启动成功!启动参数={}", () -> JSON.toJSONString(args));
    }

}
