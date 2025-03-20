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
 * 河道重点位置地理信息表(ReaFocusGeom)实体类
 *
 * @author zhy
 * @since 2022-10-26 14:06:33
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_rea_focus_geom")
public class ReaFocusGeom extends Model<ReaFocusGeom> {

    private static final long serialVersionUID = -51334131483972866L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 重点巡查位置
     */
    @TableField("focus_position")
    private String focusPosition;

    /**
     * 河道主键
     */
    @TableField("rea_id")
    private String reaId;
    /**
     * 河道名称
     */
    @TableField("rea_name")
    private String reaName;

    /**
     * 空间数据
     */
    @TableField("geom")
    private String geom;
    /**
     * 中心点-经度
     */
    @TableField("lgtd")
    private Double lgtd;

    /**
     * 中心点-纬度
     */
    @TableField("lttd")
    private Double lttd;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
