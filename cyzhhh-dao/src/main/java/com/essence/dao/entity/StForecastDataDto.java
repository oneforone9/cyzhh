package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预警报表记录实体
 *
 * @author majunjie
 * @since 2023-04-17 19:39:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_forecast_data")
public class StForecastDataDto extends Model<StForecastDataDto> {

    private static final long serialVersionUID = -82986196220998870L;
        
    /**
     * 主键
     */

    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 主键
     */
    @TableField(value = "forecast_id")
    private String forecastId;
    
    /**
     * 站点名称
     */
    @TableField("stnm")
    private String stnm;
    
    /**
     * 所属河道
     */
    @TableField("rvnm")
    private String rvnm;
    
    /**
     * 预警类型
     */
    @TableField("forecast_type")
    private String forecastType;
    
    /**
     * 原因描述
     */
    @TableField("reason")
    private String reason;
    
    /**
     * 预警状态0待处理1处理中2已处理
     */
    @TableField("forecast_state")
    private Integer forecastState;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;

}
