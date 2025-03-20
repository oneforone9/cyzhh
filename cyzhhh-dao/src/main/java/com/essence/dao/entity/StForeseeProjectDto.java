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
 * 预设调度方案实体
 *
 * @author majunjie
 * @since 2023-04-24 11:21:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_foresee_project")
public class StForeseeProjectDto extends Model<StForeseeProjectDto> {

    private static final long serialVersionUID = -11227547687224820L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 水闸或者拦河坝
     */
    @TableField("stcd")
    private String stcd;
    
    @TableField("sttp")
    private String sttp;
    
    /**
     * 水闸或者拦河坝对应的断面名称
     */
    @TableField("section_name")
    private String sectionName;
    
    /**
     * 开闸水位 ;开坝水位
     */
    @TableField("pre_water_level")
    private String preWaterLevel;
    /**
     * 闸高度;坝高度
     */
    private String high1;
    
    /**
     * 关闸水位 ;降坝水位
     */
    @TableField("open_high")
    private String openHigh;
    /**
     * 闸高度;坝高度
     */
    private String high2;
    
    /**
     * 方案id
     */
    @TableField("case_id")
    private String caseId;
    
    /**
     * 所属河流id
     */
    @TableField("rvid")
    private String rvid;
    
    /**
     * 河段名称
     */
    @TableField("rvnm")
    private String rvnm;
    
    /**
     * 闸门孔数
     */
    @TableField("holes_number")
    private String holesNumber;
    
    /**
     * 水闸高
     */
    @TableField("d_heigh")
    private String dHeigh;
    
    /**
     * 水闸宽
     */
    @TableField("d_wide")
    private String dWide;
    
    /**
     * 坝长
     */
    @TableField("b_long")
    private String bLong;
    
    /**
     * 坝高
     */
    @TableField("b_high")
    private String bHigh;
    /**
     * 序列名称
     */
    @TableField("seria_name")
    private String seriaName;
    /**
     * 1 固定值 2 策略控制
     */
    private Integer controlType;
    /**
     * 1 开 2 关
     */
    private String controlValue ;
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

    private Double wd;
}
