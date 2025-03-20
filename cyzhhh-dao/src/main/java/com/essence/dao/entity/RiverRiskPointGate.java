package com.essence.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 河道风险点
 */
@Data
public class RiverRiskPointGate {

    /**
     * 站点名称
     */
    private String stnm;

    /**
     * 站点类型
     */
    private String sttp;

    /**
     * 实时水位 (m)
     */
    private BigDecimal momentRiverLevel;

    /**
     * 实时水深 (m)
     */
    private BigDecimal momentRiverDepth;

    /**
     * 报警状态 (正常;超警戒;超最高;漫堤)
     */
    private String state;

    /**
     * 距警戒（m）
     */
    private BigDecimal fromWarning;

    /**
     * 距最高（m）
     */
    private BigDecimal fromHighest;

    /**
     * 距漫堤（m）
     */
    private BigDecimal fromOverFlow;

    /**
     * 警戒水位（m）
     */
    private BigDecimal warning;

    /**
     * 最高水位（m）
     */
    private BigDecimal highest;

    /**
     * 漫堤水位（m）
     */
    private BigDecimal overFlow;

    /**
     * 发生时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 经度
     */
    private Double lgtd;

    /**
     * 纬度
     */
    private Double lttd;

    /**
     * 风险类型
     */
    private String risk;

}
