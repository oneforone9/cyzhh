package com.essence.interfaces.model;

import lombok.Data;

import java.util.List;

/**
 * 实测降雨
 *
 * @author majunjie
 * @since 2023-03-10 16:57:04
 */

@Data
public class StCaseResRainList {


    /**
     *时间
     */
    private String time;
    /**
     * 雨量站数据
     */
    List<StCaseResRain> dataList;
}
