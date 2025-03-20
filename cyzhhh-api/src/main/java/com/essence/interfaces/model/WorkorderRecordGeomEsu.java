package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 工单地理信息记录表-记录工单创建时的重点位地理信息更新实体
 *
 * @author zhy
 * @since 2022-10-30 16:57:21
 */

@Data
public class WorkorderRecordGeomEsu extends Esu {

    private static final long serialVersionUID = 583883990066872028L;


    /**
     * 主键
     */
    private String id;

    /**
     * 重点巡查位置
     */
    private String focusPosition;

    /**
     * 河道主键
     */
    private String reaId;
    /**
     * 河道名称
     */
    private String reaName;

    /**
     * 工单主键
     */
    private String orderId;

    /**
     * 空间数据
     */
    private String geom;

    /**
     * 中心点-经度
     */
    private Double lgtd;

    /**
     * 中心点-纬度
     */
    private Double lttd;

    /**
     * 备注
     */
    private String remark;


}
