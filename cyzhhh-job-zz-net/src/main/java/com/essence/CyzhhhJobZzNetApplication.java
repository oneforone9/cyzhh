package com.essence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 该项目 主要是用于朝阳智慧河湖 中转机>请求外部网络 将外部网络数据保存到内网环境的数据库中
 */
@SpringBootApplication
@EnableScheduling
public class CyzhhhJobZzNetApplication {
	public static void main(String[] args) {
		SpringApplication.run(CyzhhhJobZzNetApplication.class, args);
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(50);
		return taskScheduler;
	}

}
