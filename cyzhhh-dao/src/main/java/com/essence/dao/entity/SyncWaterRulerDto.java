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
 * 电子水尺数据同步表实体
 *
 * @author BINX
 * @since 2023-05-24 11:46:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sync_water_ruler")
public class SyncWaterRulerDto extends Model<SyncWaterRulerDto> {

    private static final long serialVersionUID = 664183437176257453L;
    
    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 测站代码
     */
    @TableField("water_code")
    private String waterCode;
    
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;
    
    /**
     * 积水(m)
     */
    @TableField("amount")
    private BigDecimal amount;
    
    /**
     * 水务码
     */
    @TableField("swm")
    private String swm;
    
    /**
     * 入库时间-自动生成
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("rksj")
    private Date rksj;
    
    /**
     * 数据同步时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;

}
