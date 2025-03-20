package com.essence.interfaces.model;

import lombok.Data;

/**
 * 预警关联实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:23
 */
@Data

public class StForecastRelevanceList {

    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;
    /**
     * 站点编号//(边闸数据取得id)
     */
    private String stcd;

    /**
     * 站点名称
     */
    private String stnm;
    /**
     * 设备类型1边闸2水闸3.泵站4.水坝5.水位6流量7雨量
     */
    private String deviceType;
    /**
     * 在线状态1 在线  2 不在线
     */
    private String  onlineStatus;
}
