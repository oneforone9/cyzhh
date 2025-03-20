package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 河系分配表实体
 *
 * @author majunjie
 * @since 2025-01-09 09:08:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_b_river")
public class StBRiverDto extends Model<StBRiverDto> {

    private static final long serialVersionUID = 527083205561819842L;
    
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 河系名称
     */
    @TableField("river_name")
    private String riverName;

    /**
     * 管护单位
     */
    @TableField("management_unit")
    private String managementUnit;

    /**
     * 长度（km）
     */
    @TableField("self_length")
    private Double selfLength;

    /**
     * 平均宽度（m）
     */
    @TableField("average_width")
    private Double averageWidth;

    /**
     * 河流类型
     */
    @TableField("river_type")
    private String riverType;

    /**
     * 备注：用于记载该条记录的一些描述性的文字，最长不超过100个汉字。
     */
    @TableField("comments")
    private String comments;

}
