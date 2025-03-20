package com.essence.interfaces.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 实测流量
 *
 * @author majunjie
 * @since 2023-03-10 16:57:04
 */

@Data
public class StCaseResFlowList {


    /**
     *时间
     */
    private String time;
    /**
     * 测站名称
     */
    private String stnm;
    /**
     * 流量（m3/s）
     */
    private String flow;

    private Double jd;

    private Double wd;

}
