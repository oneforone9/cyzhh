package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class QualifiedConfigDTO implements Serializable {
//    /**
//     * 日期类型  1 年 2 月 3 日
//     */
//    private String dateType;
    /**
     * 设备类型 ZZ  ,VIDEO
     */
    private String deviceType;
//    /**
//     * 最小数值
//     */
//    private BigDecimal minValue;
//    /**
//     * 最大数值
//     */
//    private BigDecimal maxValue;
    /**
     * yyyy-MM
     */
    private String tm;
    /**
     * 达标基数
     */
    private Integer condNum;
}
