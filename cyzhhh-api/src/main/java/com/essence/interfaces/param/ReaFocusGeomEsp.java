package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 河道重点位置地理信息表参数实体
 *
 * @author zhy
 * @since 2022-10-26 14:06:36
 */

@Data
public class ReaFocusGeomEsp extends Esp {

    private static final long serialVersionUID = -64809995263263641L;


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
     * 空间数据
     */
    @ColumnMean("geom")
    private String geom;

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;


}
