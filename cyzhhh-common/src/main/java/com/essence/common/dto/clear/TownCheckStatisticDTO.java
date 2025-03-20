package com.essence.common.dto.clear;

import lombok.Data;

import java.io.Serializable;
@Data
public class TownCheckStatisticDTO implements Serializable {
    /***
     * 达标数
     */
    private Integer pass = 0;
    /**
     * 超标
     */
    private Integer unPass = 0;
    /**
     * 无水数
     */
    private Integer none = 0;
    /**
     * 同比超标
     */
    private Integer currentMore = 0;

    /**
     * 同比达标
     */
    private Integer currentPass = 0;
    /**
     * 同比无水
     */
    private Integer currentNone = 0;
    /**
     * 环比超标
     */
    private Integer rangeMore = 0;

    /**
     * 环比达标
     */
    private Integer rangePass = 0;
    /**
     * 环比无水
     */
    private Integer rangeNone = 0;
    /**
     * yyyy-MM-dd
     */
    private String time;
    /**
     * 断面id
     */
    private String selectionId;
}
