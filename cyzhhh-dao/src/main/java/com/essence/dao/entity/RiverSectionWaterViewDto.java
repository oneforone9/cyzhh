package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/21 17:04
 */
@Data
@TableName("river_section_water_view")
public class RiverSectionWaterViewDto extends Model<RiverSectionWaterViewDto> {

    /**
     * 案件id
     */
    @TableField("case_id")
    private String caseId;

    /**
     * 河流id
     */
    @TableField("river_id")
    private String riverId;

    /**
     * 断面名称
     */
    @TableField("section_name")
    private String sectionName;

    /**
     * 河道水位
     */
    @TableField("river_z")
    private String riverZ;

    /**
     * 步长
     */
    @TableField("step")
    private String step;

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
     * 经度
     */
    @TableField("lgtd")
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    @TableField("lttd")
    private BigDecimal lttd;
    /**
     * 步长时间
     */
    private Date stepTime;

    private String dataType;
}
