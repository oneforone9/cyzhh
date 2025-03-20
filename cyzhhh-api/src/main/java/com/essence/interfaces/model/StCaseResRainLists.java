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
public class StCaseResRainLists {


    /**
     * 测站名称
     */
    private List<String> stnmList;
    /**
     * 雨量站数据
     */
    List<StCaseResRainList> stCaseResRainList;
}
