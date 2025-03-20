package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 河道重点位置地理信息表返回实体
 *
 * @author zhy
 * @since 2022-10-26 14:06:35
 */

@Data
public class ReaFocusGeomEsr extends Esr {

    private static final long serialVersionUID = 855595938195150667L;


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
