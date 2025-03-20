package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 模型断面基础表实体
 *
 * @author BINX
 * @since 2023-04-19 18:15:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_section_model")
public class StSectionModelDto extends Model<StSectionModelDto> {

    private static final long serialVersionUID = -27749323019856840L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 断面名称
     */
    @TableField("section_name")
    private String sectionName;
    
    /**
     * 河系id
     */
    @TableField("river_id")
    private Integer riverId;

    /**
     * 左岸高程
     */
    @TableField("altitude_left")
    private BigDecimal altitudeLeft;

    /**
     * 右岸高程
     */
    @TableField("altitude_right")
    private BigDecimal altitudeRight;

    /**
     * 河底高程
     */
    @TableField("altitude_bottom")
    private BigDecimal altitudeBottom;

    /**
     * 桩号
     */
    @TableField("pile_number")
    private String pileNumber;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 经度
     */
    @TableField("lgtd")
    BigDecimal lgtd;

    /**
     * 纬度
     */
    @TableField("lttd")
    BigDecimal lttd;

}
