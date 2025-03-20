package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 乡镇考核 断面
 */
@Data
public class TownCheckDto implements Serializable {
    /**
     * 断面id
     */
    private String id;
    /**
     * 区域
     */
    private String area;
    /**
     * 河流
     */
    private String street;
    /**
     * 河流
     */
    private String river;
    /**
     * 断面名称
     */
    private String selection;
    /**
     * 年月
     */
    private String time;
    /**
     * 达标情况 0 无水 1 达标 2 不达标
     */
    private Integer status;


    /**
     * 规划目标
     */
    String target ;
    /**
     * 水质类别
     */
    private String waterType;

    /**
     * 污染物
     */
    private String pollute ;
    /**
     * 平均水质类别
     */
    private String avgType ;
    /**
     * 平均水质 达标情况
     */
    private String avgStatus ;

}
