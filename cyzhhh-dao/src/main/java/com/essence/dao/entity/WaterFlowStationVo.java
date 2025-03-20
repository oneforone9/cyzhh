package com.essence.dao.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/7/6 16:14
 */
@Data
public class WaterFlowStationVo {

    /**
     * 水位流量站编码
     */
    private String stcd;

    /**
     * 水位流量站名称
     */
    private String stnm;

    /**
     * 关联闸坝
     */
    private String gate;

    /**
     * 实时水位
     */
    BigDecimal actualTimeWaterLevel;

    /**
     * 水位名: 设计高水位(7月20日~8月15日)/设计中水位(8月16日~第二年7月19日)
     */
    String levelName;

    /**
     * 设计水位: 设计高水位(7月20日~8月15日)/设计中水位(8月16日~第二年7月19日)
     */
    String waterLevel;

    /**
     * 状态
     */
    String state;

}
