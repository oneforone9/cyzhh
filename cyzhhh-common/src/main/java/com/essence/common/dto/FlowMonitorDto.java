package com.essence.common.dto;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 入境流量监测
 */
@Data
public class FlowMonitorDto implements Serializable {
    /*
     * 平均流量
     */
    private BigDecimal avgFlow;
    /**
     * 实时流量
     */
    private List<BigDecimal> timelyFlow;
    /**
     * 日期
     */
    private List<String> dates;

}
