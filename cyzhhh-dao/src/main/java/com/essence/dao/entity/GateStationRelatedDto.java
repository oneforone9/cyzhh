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
 * 闸坝对应流量水位站表实体
 *
 * @author BINX
 * @since 2023-06-08 09:48:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "gate_station_related")
public class GateStationRelatedDto extends Model<GateStationRelatedDto> {

    private static final long serialVersionUID = -69845097561958947L;
    
    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
        
    /**
     * 闸坝名称
     */
    @TableField("gate_name")
    private String gateName;
    
    /**
     * 对应的水位站流量站测站编码
     */
    @TableField("stcd")
    private String stcd;
    
    /**
     * 对应的水位站流量站测站名称
     */
    @TableField("stnm")
    private String stnm;
    
    /**
     * 对应的测站类型(ZZ-水位站;ZQ-流量站)
     */
    @TableField("sttp")
    private String sttp;
    
    /**
     * 相对位置 (0 - 上游;1 - 下游)
     */
    @TableField("stream_loc")
    private String streamLoc;

}
