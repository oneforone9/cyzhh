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
 * 水系联调-工程调度-调度预案实体
 *
 * @author majunjie
 * @since 2023-06-02 12:39:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_engineering_dispatch")
public class StWaterEngineeringDispatchDto extends Model<StWaterEngineeringDispatchDto> {

    private static final long serialVersionUID = 397044516415953453L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 调度id
     */
    @TableField("st_id")
    private String stId;
    
    /**
     * 汛期日常
     */
    @TableField("flood_season")
    private String floodSeason;
    
    /**
     * 蓝色预警
     */
    @TableField("b_warning")
    private String bWarning;
    
    /**
     * 黄色预警
     */
    @TableField("y_warning")
    private String yWarning;
    
    /**
     * 橙色、红色预警
     */
    @TableField("r_warning")
    private String rWarning;

}
