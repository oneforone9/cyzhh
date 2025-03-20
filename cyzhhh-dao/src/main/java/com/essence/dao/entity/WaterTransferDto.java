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
 * 调水表实体
 *
 * @author BINX
 * @since 2023-05-09 09:53:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "water_transfer")
public class WaterTransferDto extends Model<WaterTransferDto> {

    private static final long serialVersionUID = 134336901501548300L;
    
    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
        
    /**
     * 水源
     */
    @TableField("source")
    private String source;
    
    /**
     * 当日调水量(万m³/日)
     */
    @TableField("transfer")
    private BigDecimal transfer;
    
    /**
     * 去水
     */
    @TableField("target")
    private String target;
    
    /**
     * 调水类型(0 - 调水/1 - 补水/2 - 退水)
     */
    @TableField("type")
    private String type;
    
    /**
     * 近期调用(0 - 近期/ 1 - 远期)
     */
    @TableField("recent_transfer")
    private Integer recentTransfer;
    
    /**
     * 定位id
     */
    @TableField("position_id")
    private String positionId;

    /**
     * 概化图
     */
    @TableField("image")
    private String image;

    /**
     * 来源补水口id
     */
    @TableField("source_port_id")
    private Long sourcePortId;

}
