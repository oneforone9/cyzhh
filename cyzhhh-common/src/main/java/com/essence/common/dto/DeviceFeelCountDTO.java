package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 设备感知
 *
 */
@Data
public class DeviceFeelCountDTO implements Serializable {
    /**
     * 总数
     */
    private Integer total;

    /**
     * 1 年  2 月  3 日
     */
    private String dateType;
    /**
     * 在线数据
     */
    private List<Integer> upValue;

    /**
     * 离线数据
     */
    private List<Integer> downValue;

    /**
     * 百分比
     */
    private List<BigDecimal> percent;




}
