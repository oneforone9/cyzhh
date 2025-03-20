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
 * 工单地理信息记录表-记录工单创建时的重点位地理信息(WorkorderRecordGeom)实体类
 *
 * @author zhy
 * @since 2022-10-30 16:57:21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_workorder_record_geom")
public class WorkorderRecordGeom extends Model<WorkorderRecordGeom> {

    private static final long serialVersionUID = -89820664868561634L;


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
     * 工单主键
     */
    @TableField("order_id")
    private String orderId;

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
