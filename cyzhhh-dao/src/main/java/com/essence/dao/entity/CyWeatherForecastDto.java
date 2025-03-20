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
 * 实体
 *
 * @author BINX
 * @since 2023-03-16 16:41:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "cy_weather_forecast")
public class CyWeatherForecastDto extends Model<CyWeatherForecastDto> {

    private static final long serialVersionUID = 914709614533618456L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 气象发布部门
     */
    @TableField("publish_department")
    private String publishDepartment;
    
    /**
     * 发布时间
     */
    @TableField("publish_time")
    private String publishTime;
    
    /**
     * 气象类型
     */
    @TableField("weather_type")
    private String weatherType;
    
    /**
     * 气象等级
     */
    @TableField("weather_level")
    private String weatherLevel;
    
    /**
     * 状态
     */
    @TableField("status")
    private String status;
    
    /**
     * 内容
     */
    @TableField("context")
    private String context;
    
    /**
     * 防护措施
     */
    @TableField("defence")
    private String defence;
    
    /**
     * 消息
     */
    @TableField("msg")
    private String msg;
    
    /**
     * 图标
     */
    @TableField("icon")
    private String icon;
    
    /**
     * 预警类型
     */
    @TableField("now_waring")
    private String nowWaring;

}
