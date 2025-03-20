package com.essence.dao.entity;

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
 * 水位流量站(对外)实体
 *
 * @author BINX
 * @since 2023-05-11 16:13:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "station_out")
public class StationOutDto extends Model<StationOutDto> {

    private static final long serialVersionUID = -73900109713402579L;
    
    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
        
    /**
     * 测站编码
     */
    @TableField("stcd")
    private String stcd;
    
    /**
     * 测站名称
     */
    @TableField("stnm")
    private String stnm;
    
    /**
     * 监测点描述
     */
    @TableField("monitor")
    private String monitor;
    
    /**
     * 所属河道id
     */
    @TableField("river_id")
    private Integer riverId;

}
