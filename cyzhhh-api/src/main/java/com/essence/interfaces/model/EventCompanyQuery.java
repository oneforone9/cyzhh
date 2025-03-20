package com.essence.interfaces.model;


import lombok.Data;

/**
 * 第三方服务公司人员配置更新实体
 *
 * @author majunjie
 * @since 2023-06-05 11:24:58
 */

@Data
public class EventCompanyQuery {
    /**
     * 1.河道管理一所2.河道管理二所
     */
    private String unitId;
    /**
     * 数据类型（1-河道绿化保洁  2-闸坝运行养护）
     */
    private Integer type;
    /**
     * 河段id
     */
    private String riverId;
}
