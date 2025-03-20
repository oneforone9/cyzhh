package com.essence.interfaces.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 预设调度方案返回实体
 *
 * @author majunjie
 * @since 2023-04-24 11:21:24
 */

@Data
public class StForeseeProjectEsr extends Esr {

    private static final long serialVersionUID = -62577670074470778L;

    private String id;


    /**
     * 水闸或者拦河坝
     */
    private String stcd;
    /**
     * 水闸DD坝SB
     */
    private String sttp;


    /**
     * 水闸或者拦河坝对应的断面名称
     */
    private String sectionName;


    /**
     * 闸前水位 ;坝前水位
     */
    private String preWaterLevel;


    /**
     * 闸门开度 ;冲坝高度
     */
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
    private String caseId;


    /**
     * 所属河流id
     */
    private String rvid;


    /**
     * 河段名称
     */
    private String rvnm;


    /**
     * 闸门孔数
     */
    private String holesNumber;


    /**
     * 水闸高
     */
    private String dHeigh;


    /**
     * 水闸宽
     */
    private String dWide;


    /**
     * 坝长
     */
    private String bLong;


    /**
     * 坝高
     */
    private String bHigh;

    private String seriaName;
    /**
     * 1 固定值 2 策略控制
     */
    private int controlType = 1;
    /**
     * 1 开 2 关
     */
    private String controlValue;
    /**
     * 闸门1宽(m)
     */

    private String holesK;

    /**
     * 闸门1高(m)
     */

    private String holesG;
    /**
     * 水坝默认充坝高度
     */
    private String bDefault;

    private Double jd;

    private  Double wd;
}
