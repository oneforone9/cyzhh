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
 * 河道信息表(ReaBase)实体类
 *
 * @author zhy
 * @since 2022-10-18 17:22:22
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_rea_base")
public class ReaBase extends Model<ReaBase> {

    private static final long serialVersionUID = 190734482441379391L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 河道名称
     */
    @TableField("rea_name")
    private String reaName;
    /**
     * 单位id
     */
    @TableField("unit_id")
    private String unitId;

    /**
     * 管理单位
     */
    @TableField("unit_name")
    private String unitName;

    /**
     * 长度(km)
     */
    @TableField("rea_length")
    private Double reaLength;

    /**
     * 宽度(m)
     */
    @TableField("rea_width")
    private Double reaWidth;

    /**
     * 行政区名称
     */
    @TableField("ad_name")
    private String adName;

    /**
     * 水面面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    @TableField("water_space")
    private Double waterSpace;

    /**
     * 绿化面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    @TableField("green_space")
    private Double greenSpace;

    /**
     * 保洁面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    @TableField("clean_space")
    private Double cleanSpace;

    /**
     * 管理面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    @TableField("manage_space")
    private Double manageSpace;

    /**
     * 起点位置
     */
    @TableField("start_position")
    private String startPosition;

    /**
     * 终点位置
     */
    @TableField("end_position")
    private String endPosition;

    /**
     * 岸别(0不分,1左岸,2右岸)
     */
    @TableField("shore")
    private String shore;

    /**
     * 河道类型（1河 2沟 3渠）
     */
    @TableField("rea_type")
    private String reaType;

    /**
     * 上级主键
     */
    @TableField("up_id")
    private String upId;

    /**
     * 河道级别（1区 2乡镇 3村）
     */
    @TableField("rea_level")
    private String reaLevel;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 河流数据
     */
    @TableField("geom")
    private String geom;
    /**
     * 河流线
     */
    @TableField("geom_line")
    private String geomLine;
    /**
     * 河流等级
     */
    @TableField("rank")
    private Integer rank;

    /**
     * 作用
     */
    @TableField("`function`")
    private String function;

    /**
     * 起点
     */
    @TableField("`begin`")
    private String begin;

    /**
     * 止点
     */
    @TableField("`end`")
    private String end;
}
