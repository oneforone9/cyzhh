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
 * 设计雨型实体
 *
 * @author majunjie
 * @since 2023-04-24 09:57:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_desig_rain_pattern")
public class StDesigRainPatternDto extends Model<StDesigRainPatternDto> {

    private static final long serialVersionUID = 125281787419773239L;
        
    /**
     * 雨型ID
     */
    @TableId(value = "design_rain_pattern_id", type = IdType.INPUT)
    private String designRainPatternId;
        
    /**
     * 雨型名称
     */
    @TableField("design_rain_pattern_name")
    private String designRainPatternName;
    
    /**
     * 小时数
     */
    @TableField("hour_count")
    private Integer hourCount;
    
    /**
     * 时间间隔（分钟）
     */
    @TableField("time_interval")
    private Integer timeInterval;
    
    /**
     * 雨型参数（参数之间用英文逗号隔开）
     */
    @TableField("param")
    private String param;
    
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;
    
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
