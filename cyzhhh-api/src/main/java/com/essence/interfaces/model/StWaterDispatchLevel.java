package com.essence.interfaces.model;


import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:29
 */

@Data
public class StWaterDispatchLevel {
    /**
     * 高水位(m) [非主汛期、非汛期（8月16日-次年7月19日）]
     */
    private String highWaterLevel;

    /**
     * 中水位(m) [主汛期（7月20日-8月15日）]
     */
    private String middleWaterLevel;

    /**
     * 时间节点
     */
    private String time;
    /**
     * 实时水位(m)
     */
    private String waterLevel;

}
