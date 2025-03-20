package com.essence.job.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.essence.common.constant.JobConstant.IDENTIFY;

@Configuration
public class ProdConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public CronJobIdentifierProvider cronJobIdentifierProvider() {
        return () -> context.getEnvironment().getProperty(IDENTIFY, "mock");
    }
}
