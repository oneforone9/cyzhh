package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class RainComposeDto implements Serializable {
    /**
     * 时段雨量
     */
    private List<BigDecimal> rainList;

    /**
     * 累计雨量
     */
    private List<BigDecimal> addList;
    /**
     * 时间
     */
    private List<String> timeList;
    /**
     * 累计雨量
     */
    private BigDecimal addRainNum;
}
