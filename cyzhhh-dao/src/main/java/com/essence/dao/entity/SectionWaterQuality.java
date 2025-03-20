package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 断面水质情况表
 *
 * @author bird
 * @date 2022/11/19 14:37
 * @Description
 */
@Data
@TableName("section_water_quality")
public class SectionWaterQuality implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 断面主键
     */
    @TableField("section_id")
    private Integer sectionId;

    /**
     * 水质期间(年月)
     */
    @TableField("quality_period")
    private String qualityPeriod;

    /**
     * 水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;6 无水/结冰；)
     */
    @TableField("quality_level")
    private Integer qualityLevel;

    /**
     * 新增时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;

    /**
     * 更新者
     */
    @TableField("updater")
    private String updater;

    /**
     * 备注 水质等级
     */
    @TableField("level_remark")
    private String levelRemark;


    /**
     * 平均水质等级
     */
    @TableField("average_level")
    private Integer averageLevel;
}
