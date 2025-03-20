package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水位状态 统计
 */
@Data
@TableName("st_water_position_static")
public class WaterPositionStatisticEntity implements Serializable {
    /**
     * 正常数量
     */
    private Integer normalNum;
    /**
     * 正常数量百分比
     */
    private BigDecimal normalPercent;
    /**
     * 超警戒数量
     */
    private Integer moreForbidNum;
    /**
     * 超警戒百分比
     */
    private BigDecimal moreForbidPercent;
    /**
     * 最大水位数量
     */
    private Integer mostPositionNum;
    /**
     * 最大水位百分比
     */
    private BigDecimal mostPositionPercent;

}
