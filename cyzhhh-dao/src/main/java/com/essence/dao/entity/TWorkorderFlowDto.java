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
 * 工单处理过程表实体
 *
 * @author majunjie
 * @since 2023-06-07 10:31:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_workorder_flow")
public class TWorkorderFlowDto extends Model<TWorkorderFlowDto> {

    private static final long serialVersionUID = -81832884632597759L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 工单主键
     */
    @TableField("order_id")
    private String orderId;
    
    /**
     * 当前操作人主键
     */
    @TableField("person_id")
    private String personId;
    
    /**
     * 当前操作人
     */
    @TableField("person_name")
    private String personName;
    
    /**
     * 工单操作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("order_time")
    private Date orderTime;
    
    /**
     * 操作 1-生产工单   2-派发工单  3-开启工单  4-完成工单   5-审核工单  7-催办工单  -1 终止工单  -2 审核工单
     */
    @TableField("operation")
    private String operation;
}
