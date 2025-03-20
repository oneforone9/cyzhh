package com.essence.job.validate;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.essence.common.constant.JobConstant.*;


/**
 * 用于定时任务启动校验
 *
 * @author huangyong
 * Create at 2024/7/25
 */
@Component
@Log4j2
@Profile("prod")
@Order(1)
public class StartupJobValidator implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) {
        String identifier = context.getEnvironment().getProperty(IDENTIFY);
        if (identifier == null || identifier.trim().isEmpty()) {
            log.error(jobErrorTip);
            System.exit(1);
        }
        List<String> list = Arrays.asList(BACK_END, BACK_JOB);
        if (!list.contains(identifier)) {
            log.error(jobErrorTip2);
            System.exit(1);
        }
    }

}
