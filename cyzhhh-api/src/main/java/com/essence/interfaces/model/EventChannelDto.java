package com.essence.interfaces.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class EventChannelDto implements Serializable {
    /**
     * 案件数量
     */
    private List<Integer> caseNum;
    /**
     * 案件占比
     */
    private List<BigDecimal> casePercent;
    /**
     * 上月环比
     */
    private List<BigDecimal> compareLast;

}
