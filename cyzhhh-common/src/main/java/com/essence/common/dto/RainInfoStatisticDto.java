package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RainInfoStatisticDto implements Serializable {
    /**
     * 今年累计降雨量
     */
    private BigDecimal addCurrentRain;
    /**
     * 去年累计降雨量
     */
    private BigDecimal addLastRain;
    /**
     * 同比去年百分比
     */
    private BigDecimal currentPercent;
    /**
     * 今年汛期累计
     */
    private BigDecimal addXQCurrentRain;
    /**
     * 去年汛期累计
     */
    private BigDecimal lastXQAddRain;
    /**
     * 汛期同比百分比
     */
    private BigDecimal xqPercent;
    /**
     * 全区日平均雨量
     */
    private BigDecimal avgRainNum;
    /**
     * 日最大雨量站点
     */
    private String stnm;
    /**
     * 日最大雨量站雨量
     */
    private BigDecimal rainNum;
    /**
     * 日最小雨量站点
     */
    private String stnmMin;
    /**
     * 日最小雨量站雨量
     */
    private BigDecimal rainNumMin;
}
