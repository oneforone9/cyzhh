package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 网格数据
 */
@Data
@TableName("grid_rain_data")
public class GridRainDataEntity {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private BigDecimal h21;

    private BigDecimal h22;

    private BigDecimal h23;

    private BigDecimal h0;

    private BigDecimal h1;

    private BigDecimal h2;

    private BigDecimal h3;

    private BigDecimal h4;

    private BigDecimal h5;

    private BigDecimal h6;

    private BigDecimal h7;

    private BigDecimal h8;

    private BigDecimal h9;

    private BigDecimal h10;

    private BigDecimal h11;

    private BigDecimal h12;
}
