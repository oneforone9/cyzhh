package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhy
 * @since 2023/1/4 18:22
 */
@Data
public class StatisticsDto implements Serializable {

    private static final long serialVersionUID = 4389968662386952390L;
    /**
     * 类型
     */
    private String type;
    /**
     * 值
     */
    private int value;
    /**
     * 比率（小数由前端处理）
     */
    private Double rate;

    private String updateDate;
}
