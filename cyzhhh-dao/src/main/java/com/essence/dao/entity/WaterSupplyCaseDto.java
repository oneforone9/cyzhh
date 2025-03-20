package com.essence.dao.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 补水口案件计算入参表实体
 *
 * @author BINX
 * @since 2023-05-04 19:14:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "water_supply_case")
public class WaterSupplyCaseDto extends Model<WaterSupplyCaseDto> {

    private static final long serialVersionUID = -84705096595315353L;
    
    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
        
    /**
     * 案件id
     */
    @TableField("case_id")
    private String caseId;
    
    /**
     * 河道名称
     */
    @TableField("river_name")
    private String riverName;
    
    /**
     * 补水口名称
     */
    @TableField("water_port_name")
    private String waterPortName;
    
    /**
     * 供水能力（m³/s）
     */
    @TableField("supply")
    private BigDecimal supply;
    
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @TableField("lgtd")
    private BigDecimal lgtd;
    
    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @TableField("lttd")
    private BigDecimal lttd;
    
    /**
     * 断面名称
     */
    @TableField("section_name")
    private String sectionName;
    
    /**
     * 序列名称
     */
    @TableField("seria_name")
    private String seriaName;

    /**
     * 补水方式 (0 - 默认固定流量; 1 - 时间序列)
     */
    @TableField("supply_way")
    private int supplyWay;

    /**
     * 时间序列流量
     */
    @TableField("time_supply")
    private String timeSupply;

}
