package com.essence.interfaces.model;


import lombok.Data;

/**
 * 实测雨量数据
 *
 * @author majunjie
 * @since 2023-03-14 14:57:32
 */

@Data
public class StCaseResRain {

    /**
     * 测站名称-断面名称
     */
    private String stnm;
    /**
     * 小时雨量
     */
    private Double rain;

    private Double jd;

    private Double wd;
}
