package com.essence.interfaces.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class EventBaseDTO implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 所属河道主键
     */
    private String reaId;

    /**
     * 所属河道名称
     */
    private String reaName;

    /**
     * 管辖单位主键
     */
    private String unitId;

    /**
     * 管辖单位
     */
    private String unitName;
    /**
     * 问题描述
     */
    private List<String> problemDesc;

    /**
     * 案件时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date eventTime;

    private String orderId;

}
