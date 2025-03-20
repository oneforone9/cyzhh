package com.essence.interfaces.model;


import lombok.Data;

/**
 * 预设调度方案返回实体
 *
 * @author majunjie
 * @since 2023-04-24 11:21:24
 */

@Data
public class StForeseeProjectQuery  {


    /**
     * 水闸或者拦河坝名称
     */
    private String stcd;

    /**
     * 水闸DD坝SB
     */
    private String sttp;


    /**
     * 水闸或者拦河坝对应的断面名称
     */
    private String sectionName;


    /**
     * 闸前水位 ;坝前水位
     */
    private String preWaterLevel;


    /**
     * 闸门开度 ;冲坝高度
     */
    private String openHigh;


    /**
     * 方案id
     */
    private String caseId;


    /**
     * 所属河流id
     */
    private String rvid;


    /**
     * 河段名称
     */
    private String rvnm;


    /**
     * 闸门孔数
     */
    private String holesNumber;


    /**
     * 水闸高
     */
    private String dHeigh;


    /**
     * 水闸宽
     */
    private String dWide;


    /**
     * 坝长
     */
    private String bLong;


    /**
     * 坝高
     */
    private String bHigh;

}
