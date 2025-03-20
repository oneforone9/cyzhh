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
 * 闸坝泵id 和 采集sn码转换对接一下实体
 *
 * @author BINX
 * @since 2023-05-25 18:06:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_ga_convert")
public class StGaConvertDto extends Model<StGaConvertDto> {

    private static final long serialVersionUID = 102394770461196925L;
    
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    @TableField("sn")
    private String sn;
    
    @TableField("stcd")
    private String stcd;

}
