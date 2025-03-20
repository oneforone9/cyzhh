package com.essence.interfaces.model;


import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:29
 */

@Data
public class StWaterDispatchLevelList {
    /**
     * 站点编码主键
     */
    private Integer stcdId;
    /**
     * 站点编码闸坝
     */
    private String stcd;

    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;
    /**
     * 调度状态
     */
    private String dType;
    /**
     * 调度指令
     */
    private String dOrder;
    /**
     * 站点名称
     */
    private String stnm;
    /**
     * 实时水位(m)
     */
    private String waterLevel;
    /**
     * 高水位(m) [非主汛期、非汛期（8月16日-次年7月19日）]
     */
    private String highWaterLevel;

    /**
     * 中水位(m) [主汛期（7月20日-8月15日）]
     */
    private String middleWaterLevel;
}
