package com.essence.interfaces.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EventAnalysisDto implements Serializable {
    /**
     * 水环境
     */
    private Integer waterEnv;
    /**
     * 河道设施
     */
    private Integer riverRoundDevice;
    /**
     * 涉河工程有关活动
     */
    private Integer riverProject;

    /**
     * 水环境
     */
    private BigDecimal waterEnvPercent;
    /**
     * 河道设施
     */
    private BigDecimal riverRoundDevicePercent;
    /**
     * 涉河工程有关活动
     */
    private BigDecimal riverProjectPercent;
    /**
     * 水环境-上月环比
     */
    private BigDecimal waterEnvPercentPre;
    /**
     * 河道设施-上月环比
     */
    private BigDecimal riverRoundDevicePercentPre;
    /**
     * 涉河工程有关活动-上月环比
     */
    private BigDecimal riverProjectPercentPre;
}
