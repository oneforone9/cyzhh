package com.essence.job.validate;

/**
 * 区分环境获取定时任务变量
 * @author huangyong
 * Create at 2024/7/25
 */
public interface CronJobIdentifierProvider {
    String getCronJobIdentifier();
}
