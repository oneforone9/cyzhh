package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 工单地理信息记录表-记录工单创建时的重点位地理信息参数实体
 *
 * @author zhy
 * @since 2022-10-30 16:57:21
 */

@Data
public class WorkorderRecordGeomEsp extends Esp {

    private static final long serialVersionUID = -37874744580804925L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 重点巡查位置
     */
    @ColumnMean("focus_position")
    private String focusPosition;

    /**
     * 河道主键
     */
    @ColumnMean("rea_id")
    private String reaId;
    /**
     * 河道名称
     */
    @ColumnMean("rea_name")
    private String reaName;

    /**
     * 工单主键
     */
    @ColumnMean("order_id")
    private String orderId;

    /**
     * 空间数据
     */
    @ColumnMean("geom")
    private String geom;

    /**
     * 中心点-经度
     */
    @ColumnMean("lgtd")
    private Double lgtd;

    /**
     * 中心点-纬度
     */
    @ColumnMean("lttd")
    private Double lttd;

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;


}
