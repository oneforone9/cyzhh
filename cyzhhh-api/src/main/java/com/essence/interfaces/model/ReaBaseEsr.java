package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 河道信息表返回实体
 *
 * @author zhy
 * @since 2022-10-18 17:22:23
 */

@Data
public class ReaBaseEsr extends Esr {

    private static final long serialVersionUID = -62943585153706463L;


    /**
     * 主键
     */
    private String id;

    /**
     * 河道名称
     */
    private String reaName;
    /**
     * 单位id
     */
    private String unitId;

    /**
     * 管理单位
     */
    private String unitName;

    /**
     * 长度(km)
     */
    private Double reaLength;

    /**
     * 宽度(m)
     */
    private Double reaWidth;

    /**
     * 行政区名称
     */
    private String adName;

    /**
     * 水面面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    private Double waterSpace;

    /**
     * 绿化面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    private Double greenSpace;

    /**
     * 保洁面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    private Double cleanSpace;

    /**
     * 管理面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    private Double manageSpace;

    /**
     * 起点位置
     */
    private String startPosition;

    /**
     * 终点位置
     */
    private String endPosition;

    /**
     * 岸别(0不分,1左岸,2右岸)
     */
    private String shore;

    /**
     * 河道类型（1河 2沟 3渠）
     */
    private String reaType;

    /**
     * 上级主键
     */
    private String upId;

    /**
     * 河道级别（1区 2乡镇 3村）
     */
    private String reaLevel;

    /**
     * 备注
     */
    private String remark;
    /**
     * 河流等级
     */
    private Integer rank;
    /**
     * 作用
     */
    private String function;
    /**
     * 起点
     */
    private String begin;
    /**
     * 止点
     */
    private String end;

}
