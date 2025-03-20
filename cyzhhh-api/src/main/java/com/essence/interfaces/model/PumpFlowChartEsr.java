package com.essence.interfaces.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/26 10:54
 */
@Data
public class PumpFlowChartEsr {

    /**
     * 测站编码
     */
    String stcd;

    /**
     * 测站名称
     */
    String stnm;

    /**
     * 测站位置
     */
    String stlc;

    /**
     * 河流Id
     */
    Integer riverId;

    /**
     * 经度
     */
    Double lgtd;

    /**
     * 纬度
     */
    Double lttd;

    /**
     * 瞬时流量
     */
    List<String> flowRateList;

    /**
     * 累计流量
     */
    List<String> addFlowRateList;

    /**
     * 时间
     */
    List<String> dateList;
}
