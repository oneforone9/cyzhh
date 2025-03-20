package com.essence.interfaces.dot;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 河流断面数据返回实体
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/21 16:33
 */
@Data
public class RiverSectionViewDto {

    /**
     * 案件Id
     */
    String caseId;

    /**
     * 河流Id
     */
    String riverId;

    /**
     * 断面名称
     */
    String sectionName;

    /**
     * 河道断面水位
     */
    BigDecimal riverZ;

    /**
     * 步长
     */
    String step;

    /**
     * 左岸高程
     */
    BigDecimal altitudeLeft;

    /**
     * 右岸高程
     */
    BigDecimal altitudeRight;

    /**
     * 河底高程
     */
    BigDecimal altitudeButtom;

    /**
     * 经度
     */
    BigDecimal lgtd;

    /**
     * 纬度
     */
    BigDecimal lttd;

    /**
     * 水深(河道断面水位与河底高程取差值)
     */
    BigDecimal depth;

    /**
     * 闸坝ID
     */
    Integer gateId;

    /**
     * 闸坝编码
     */
    String gateStcd;

    /**
     * 闸坝名称
     */
    String gateName;

    /**
     * 闸坝经度
     */
    Double gateLgtd;

    /**
     * 闸坝纬度
     */
    Double gateLttd;

    /**
     * 警戒水位
     */
    BigDecimal gateWrz;

    /**
     * 最高水位
     */
    BigDecimal gateBhtz;

    /**
     * 距警戒（m）
     */
    private BigDecimal fromWarning;

    /**
     * 距最高（m）
     */
    private BigDecimal fromHighest;

    /**
     * 泵站ID
     */
    Integer pumpId;

    /**
     * 泵站编码
     */
    String pumpStcd;

    /**
     * 泵站名称
     */
    String pumpName;

    /**
     * 泵站经度
     */
    Double pumpLgtd;

    /**
     * 泵站纬度
     */
    Double pumpLttd;

    /**
     * 水位站编码
     */
    String waterStcd;

    /**
     * 水位站名称
     */
    String waterName;

    /**
     * 水位站经度
     */
    BigDecimal waterLgtd;

    /**
     * 水位站纬度
     */
    BigDecimal waterLttd;

    /**
     * 流量站编码
     */
    String flowStcd;

    /**
     * 流量站名称
     */
    String flowName;

    /**
     * 流量站经度
     */
    BigDecimal flowLgtd;

    /**
     * 流量站纬度
     */
    BigDecimal flowLttd;
    /**
     * 实时水位
     */
    BigDecimal factDeep;
    /**
     * 高程
     */
    private String dtmel;
}
