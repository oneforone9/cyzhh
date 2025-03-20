package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 预设调度方案参数实体
 *
 * @author majunjie
 * @since 2023-04-24 11:21:22
 */

@Data
public class StForeseeProjectEsp extends Esp {

    private static final long serialVersionUID = -47604609260328381L;

    @ColumnMean("id")
    private String id;
    /**
     * 水闸或者拦河坝
     */
    @ColumnMean("stcd")
    private String stcd;

    /**
     * 水闸DD坝SB
     */
    @ColumnMean("sttp")
    private String sttp;
    /**
     * 水闸或者拦河坝对应的断面名称
     */
    @ColumnMean("section_name")
    private String sectionName;
    /**
     * 闸前水位 ;坝前水位
     */
    @ColumnMean("pre_water_level")
    private String preWaterLevel;
    /**
     * 闸门开度 ;冲坝高度
     */
    @ColumnMean("open_high")
    private String openHigh;

    /**
     * 闸高度;坝高度
     */
    private String high1;
    /**
     * 闸高度;坝高度
     */
    private String high2;
    /**
     * 方案id
     */
    @ColumnMean("case_id")
    private String caseId;
    /**
     * 所属河流id
     */
    @ColumnMean("rvid")
    private String rvid;
    /**
     * 河段名称
     */
    @ColumnMean("rvnm")
    private String rvnm;
    /**
     * 闸门孔数
     */
    @ColumnMean("holes_number")
    private String holesNumber;
    /**
     * 水闸高
     */
    @ColumnMean("d_heigh")
    private String dHeigh;
    /**
     * 水闸宽
     */
    @ColumnMean("d_wide")
    private String dWide;
    /**
     * 坝长
     */
    @ColumnMean("b_long")
    private String bLong;
    /**
     * 坝高
     */
    @ColumnMean("b_high")
    private String bHigh;
    @ColumnMean("seria_name")
    private String seriaName;
    /**
     * 1 固定值 2 策略控制
     */
    @ColumnMean("control_type")
    private int controlType;
    /**
     * 1 开 2 关
     */
    @ColumnMean("control_value")
    private String controlValue;
    /**
     * 水坝默认充坝高度
     */
    private String bDefault;
}
