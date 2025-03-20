package com.essence.interfaces.dot;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 河道信息表(ReaBase)实体类
 *
 * @author zhy
 * @since 2022-10-18 17:22:22
 */

@Data
@Accessors(chain = true)
public class ReaBaseDto {

    private static final long serialVersionUID = 190734482441379391L;


    /**
     * 主键
     */
    private String id;

    /**
     * 河道名称
     */
    private String reaName;
    /**
     * 单位id
     */
    private String unitId;

    /**
     * 管理单位
     */
    private String unitName;

    /**
     * 长度(km)
     */
    private BigDecimal reaLength;

    /**
     * 面积
     */
    private BigDecimal reaWidth;


    /**
     * 河流描述
     */
    private String geom;
    /**
     * 河水流速 1 慢 2 正常 3 快
     */
    private Integer waterRate;
    /**
     * 河流 线
     */
    private String geomLine;
}
