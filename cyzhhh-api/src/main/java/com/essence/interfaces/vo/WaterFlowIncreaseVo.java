package com.essence.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 水位流量上升增大对比Vo
 *
 * @author huangyong
 * Create at 2024/7/17
 */
@Data
public class WaterFlowIncreaseVo {

    /**
     * 水位上升-数量一小时
     */
    private Integer waterIncreaseNmOneHour;

    /**
     * 水位上升-数量一天
     */
    private Integer waterIncreaseNmOneDay;

    /**
     * 水位上升-最大值一小时
     */
    private BigDecimal waterMaxOneHour;

    /**
     * 水位上升-最大值一天
     */
    private BigDecimal waterMaxOneDay;

    /**
     * 水位上升-最大站点名称一小时
     */
    private String waterStationNameOneHour;

    /**
     * 水位上升-最大站点名称一天
     */
    private String waterStationNameOneDay;

    /**
     * 水位上升-值一小时
     */
    private BigDecimal waterValueOneHour;

    /**
     * 水位上升-值一天
     */
    private BigDecimal waterValueOneDay;


    /**
     * 流量增大-数量一小时
     */
    private Integer flowIncreaseNmOneHour;

    /**
     * 流量增大-数量一天
     */
    private Integer flowIncreaseNmOneDay;

    /**
     * 流量增大-最大值一小时
     */
    private BigDecimal flowMaxOneHour;

    /**
     * 流量增大-最大值一天
     */
    private BigDecimal flowMaxOneDay;

    /**
     * 流量增大-最大站点名称一小时
     */
    private String flowStationNameOneHour;

    /**
     * 流量增大-最大站点名称一天
     */
    private String flowStationNameOneDay;

    /**
     * 流量增大-增大值一小时
     */
    private BigDecimal flowValueOneHour;

    /**
     * 流量增大-大值一天
     */
    private BigDecimal flowValueOneDay;
}
