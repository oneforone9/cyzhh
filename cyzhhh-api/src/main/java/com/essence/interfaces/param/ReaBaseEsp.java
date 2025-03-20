package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 河道信息表参数实体
 *
 * @author zhy
 * @since 2022-10-18 17:22:24
 */

@Data
public class ReaBaseEsp extends Esp {

    private static final long serialVersionUID = 232444320134196857L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 河道名称
     */
    @ColumnMean("rea_name")
    private String reaName;

    /**
     * 单位id
     */
    @ColumnMean("unit_id")
    private String unitId;

    /**
     * 管理单位
     */
    @ColumnMean("unit_name")
    private String unitName;

    /**
     * 长度(km)
     */
    @ColumnMean("rea_length")
    private Double reaLength;

    /**
     * 宽度(m)
     */
    @ColumnMean("rea_width")
    private Double reaWidth;

    /**
     * 行政区名称
     */
    @ColumnMean("ad_name")
    private String adName;

    /**
     * 水面面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    @ColumnMean("water_space")
    private Double waterSpace;

    /**
     * 绿化面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    @ColumnMean("green_space")
    private Double greenSpace;

    /**
     * 保洁面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    @ColumnMean("clean_space")
    private Double cleanSpace;

    /**
     * 管理面积(m2)(分岸别，左右岸共用数据只填右岸)
     */
    @ColumnMean("manage_space")
    private Double manageSpace;

    /**
     * 起点位置
     */
    @ColumnMean("start_position")
    private String startPosition;

    /**
     * 终点位置
     */
    @ColumnMean("end_position")
    private String endPosition;

    /**
     * 岸别(0不分,1左岸,2右岸)
     */
    @ColumnMean("shore")
    private String shore;

    /**
     * 河道类型（1河 2沟 3渠）
     */
    @ColumnMean("rea_type")
    private String reaType;

    /**
     * 上级主键
     */
    @ColumnMean("up_id")
    private String upId;

    /**
     * 河道级别（1区 2乡镇 3村）
     */
    @ColumnMean("rea_level")
    private String reaLevel;

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;

    /**
     * 河流等级
     */
    @ColumnMean("rank")
    private Integer rank;

    /**
     * 作用
     */
    @ColumnMean("function")
    private String function;

    /**
     * 起点
     */
    @ColumnMean("begin")
    private String begin;

    /**
     * 止点
     */
    @ColumnMean("end")
    private String end;


}
