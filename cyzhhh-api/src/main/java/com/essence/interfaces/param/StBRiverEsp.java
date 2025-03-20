package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 河系分配表参数实体
 *
 * @author majunjie
 * @since 2025-01-09 09:08:05
 */

@Data
public class StBRiverEsp extends Esp {

    private static final long serialVersionUID = -64416203572815478L;

            @ColumnMean("id")
    private Integer id;
    /**
     * 河系名称
     */        @ColumnMean("river_name")
    private String riverName;
    /**
     * 管护单位
     */        @ColumnMean("management_unit")
    private String managementUnit;
    /**
     * 长度（km）
     */        @ColumnMean("self_length")
    private Double selfLength;
    /**
     * 平均宽度（m）
     */        @ColumnMean("average_width")
    private Double averageWidth;
    /**
     * 河流类型
     */        @ColumnMean("river_type")
    private String riverType;
    /**
     * 备注：用于记载该条记录的一些描述性的文字，最长不超过100个汉字。
     */        @ColumnMean("comments")
    private String comments;


}
