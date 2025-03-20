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
 * 补水口基础表实体
 *
 * @author BINX
 * @since 2023-02-22 17:13:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_port")
public class StWaterPortDto extends Model<StWaterPortDto> {

    private static final long serialVersionUID = -50844056909549442L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 是否上图（1-是， 0-否）
     */
    @TableField("ismap")
    private Integer ismap;
        
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
     * 补水口位置
     */
    @TableField("water_address")
    private String waterAddress;
    
    /**
     * 供水能力（万m3/日）
     */
    @TableField("supply")
    private Double supply;
    
    /**
     * 水源
     */
    @TableField("source")
    private String source;
    
    /**
     * 现况供水量
     */
    @TableField("supply_current")
    private Double supplyCurrent;
    
    /**
     * 放水口管径
     */
    @TableField("fskgj")
    private String fskgj;
    
    /**
     * 放水口管径
     */
    @TableField("fskgj_c")
    private String fskgjC;
    
    /**
     * 管底高程
     */
    @TableField("socle_altitude")
    private Double socleAltitude;
    
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @TableField("lgtd")
    private Double lgtd;
    
    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @TableField("lttd")
    private Double lttd;
    
    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

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
}
