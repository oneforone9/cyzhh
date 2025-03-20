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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 *
 * @author BINX
 * @since 2023-04-22 10:55:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_forecast_section")
public class StForecastSectionDto extends Model<StForecastSectionDto> {

    private static final long serialVersionUID = 305473949726815701L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 类型 ZZ 水位站 ZQ流量站
     */
    @TableField("sttp")
    private String sttp;
    
    /**
     * 录入时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("date")
    private Date date;
    
    /**
     * 站点名称
     */
    @TableField("section_name")
    private String sectionName;
    
    /**
     * 站点数据值
     */
    @TableField("value")
    private String value;
    
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 方案id
     */
    @TableField("case_id")
    private String caseId;
    /**
     * 流量1水位2
     */
    @TableField("type")
    private String type;
    /**
     * 序列名称
     */
    @TableField("seria_name")
    private String seriaName;
    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @TableField("wd")
    private BigDecimal wd;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @TableField("jd")
    private BigDecimal jd;
}
