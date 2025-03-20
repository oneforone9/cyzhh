package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class YearCountStatisticDto implements Serializable {
    /**
     * 接诉即办
     */
    private Integer answerDeal;
    /**
     * 区用水
     */
    private BigDecimal water;
    /**
     * 用户取水总量
     */
    private BigDecimal userWater;
    /**
     * 机井管理收费
     */
    private BigDecimal mechain;
    /**
     * 行政处罚案件
     */
    private Integer punish;


}
