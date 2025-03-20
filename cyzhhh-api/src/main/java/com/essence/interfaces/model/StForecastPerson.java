package com.essence.interfaces.model;

import lombok.Data;

/**
 * 预警报表--关联复制人
 *
 * @author majunjie
 * @since 2023-04-17 19:38:23
 */
@Data

public class StForecastPerson {
    /**
     * 职务名称
     */

    private String deviceType;

    /**
     * 负责人
     */

    private String fzr;
    /**
     * 联系电话
     */

    private String phone;
}
