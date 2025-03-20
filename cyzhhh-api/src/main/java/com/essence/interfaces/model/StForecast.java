package com.essence.interfaces.model;

import lombok.Data;

/**
 * 预警报表实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:23
 */
@Data

public class StForecast  {
    /**
     * 设备类型1闸坝2泵站3水位站4流量站
     */
    private String deviceType;

    /**
     * 设备主键（闸坝才有）
     */
    private Integer stcdId;

    /**
     * 河道编号
     */
    private String river;
}
