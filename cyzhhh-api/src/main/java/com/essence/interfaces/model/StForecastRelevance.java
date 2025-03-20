package com.essence.interfaces.model;

import lombok.Data;

/**
 * 预警关联实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:23
 */
@Data

public class StForecastRelevance {

    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;
}
