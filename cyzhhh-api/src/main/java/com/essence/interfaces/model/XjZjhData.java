package com.essence.interfaces.model;


import lombok.Data;

/**
 * 设备周计划返回实体
 *
 * @author majunjie
 * @since 2025-01-08 15:52:42
 */

@Data
public class XjZjhData  {
    /**
     * 起始时间必须是周一yyyy-mm-dd
     */
    private String startTime;


    /**
     * 结束时间必须是周日yyyy-mm-dd
     */
    private String endTime;
    /**
     * 年份
     */
    private Integer year;
}
