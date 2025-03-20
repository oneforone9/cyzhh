package com.essence.interfaces.model;

import lombok.Data;

/**
 * 实测流量
 *
 * @author majunjie
 * @since 2023-03-10 16:57:04
 */

@Data
public class StCaseResWaterList {


    /**
     *时间
     */
    private String time;
    /**
     * 测站名称
     */
    private String stnm;
    /**
     * 水位m
     */
    private String water;

    private Double jd;

    private Double wd;

}
