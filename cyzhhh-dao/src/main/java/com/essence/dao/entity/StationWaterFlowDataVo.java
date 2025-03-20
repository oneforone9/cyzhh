package com.essence.dao.entity;

import lombok.Data;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/7/7 11:04
 */
@Data
public class StationWaterFlowDataVo {

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
    /**
     * 流量
     */
    private String flow;
}
