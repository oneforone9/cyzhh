package com.essence.interfaces.dot;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WaterInfoDto implements Serializable {
    /**
     * 水位信息
     */
    private String waterLevel;
    /**
     * 流量信息
     */
    private String waterRate;
}
