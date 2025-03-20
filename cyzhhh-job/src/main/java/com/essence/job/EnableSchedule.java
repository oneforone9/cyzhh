package com.essence.job;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * 此类仅用于启动定时任务
 * @author zhy
 * @since 2022/10/21 15:41
 */
@Component
@EnableScheduling
public class EnableSchedule {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(30);
        return taskScheduler;
    }
}
