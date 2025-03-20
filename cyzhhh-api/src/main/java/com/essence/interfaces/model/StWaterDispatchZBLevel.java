package com.essence.interfaces.model;


import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:29
 */

@Data
public class StWaterDispatchZBLevel {

    /**
     * 时间节点
     */
    private String time;
    /**
     * 闸前水位(m)
     */
    private String waterLevelQ;
    /**
     * 闸后水位(m)
     */
    private String waterLevelH;

}
