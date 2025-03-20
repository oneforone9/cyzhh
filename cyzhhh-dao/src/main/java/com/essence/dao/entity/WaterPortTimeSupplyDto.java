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
 * 补水口导入时间序列流量数据无caseId回显实体
 *
 * @author BINX
 * @since 2023-05-06 15:45:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "water_port_time_supply")
public class WaterPortTimeSupplyDto extends Model<WaterPortTimeSupplyDto> {

    private static final long serialVersionUID = -18049674560061603L;
    
    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
        
    /**
     * 补水口名称
     */
    @TableField("water_port_name")
    private String waterPortName;
    
    /**
     * 时间序列流量JSON
     */
    @TableField("timeSupply")
    private String timesupply;

}
