package com.essence.tcp;


import com.essence.tcp.nettyServer.core.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * cuir
 * 此模块用于 朝阳项目的5个补水泵站 对接
 * 采用modbus-rtu方式
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class IotCyzhhhPumpModbusRtuApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(IotCyzhhhPumpModbusRtuApplication.class, args);
        log.info("朝阳项目的5个补水泵站对接,启动成功!");
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(50);
        return taskScheduler;
    }

    @Async
    @Override
    public void run(String... args) throws Exception {
        /**
         * 使用异步注解方式启动netty服务端服务
         * 211.103.210.106:18090
         */
        log.info("启动netty服务端服务");
        NettyServer.init();
//        new BootNettyServer().bind("172.16.52.5",19902);
//        new BootNettyServer().bind("127.0.0.1",19902);
    }
}

