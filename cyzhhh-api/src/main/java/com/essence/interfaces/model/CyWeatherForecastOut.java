package com.essence.interfaces.model;

import lombok.Data;

/**
 * @author majunjie
 * @since 2023-03-16 16:41:57
 */

@Data
public class CyWeatherForecastOut {

    /**
     * 气象发布部门
     */
    private String publishDepartment;


    /**
     * 发布时间
     */
    private String publishTime;


    /**
     * 气象类型
     */
    private String weatherType;


    /**
     * 气象等级
     */
    private String weatherLevel;


    /**
     * 状态
     */
    private String status;


    /**
     * 内容
     */
    private String context;


    /**
     * 防护措施
     */
    private String defence;


    /**
     * 消息
     */
    private String msg;


    /**
     * 图标
     */
    private String icon;


    /**
     * 预警类型
     */
    private String nowWaring;


}
