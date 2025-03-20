package com.essence.common.dto.clear;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ClearWeekStatisticDTO implements Serializable {
    /**
     * 河系 达标占比
     */
    private BigDecimal riverPercent;
    /**
     * 涉及河系个数
     */
    private Integer riverNum;
    /**
     * 最优河系流域
     */
    private String mvRiver;
    /**
     * 水质 百分比
     */
    private BigDecimal waterPercent;
    /**
     * 检测水质数
     */
    private Integer waterNum;
    /**
     * 水质最优水样
     */
    private String mvWater;
    /**
     * 周  格式 yyyy-周（43）
     */
    private String week;
    /**
     * yyyy-MM-dd
     */
    private String time;
}
