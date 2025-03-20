package com.essence.interfaces.model;


import lombok.Data;

/**
 * 第三方服务公司人员配置更新实体
 *
 * @author majunjie
 * @since 2023-06-05 11:24:58
 */

@Data
public class EventCompanySelect {

    /**
     * 公司名称
     */

    private String company;

    /**
     * 服务年份
     */

    private String serviceYear;

    /**
     * 所属管辖单位
     */

    private String unitName;
    /**
     * 当前页码
     */

    private Integer current;
    /**
     * 大小
     */
    private Integer size;
    /**
     * 数据类型（1-河道绿化保洁  2-闸坝运行养护）
     */
    private Integer type;
}
