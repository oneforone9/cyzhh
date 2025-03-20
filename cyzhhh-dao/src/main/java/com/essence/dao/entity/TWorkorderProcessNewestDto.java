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
 * @author liwy
 * @since 2023-08-01 16:59:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_workorder_process_newest")
public class TWorkorderProcessNewestDto extends Model<TWorkorderProcessNewestDto> {

    private static final long serialVersionUID = 164418754716341685L;
        
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
     * 当前处理人主键
     */
    @TableField("person_id")
    private String personId;
    
    /**
     * 当前处理人
     */
    @TableField("person_name")
    private String personName;
    
    /**
     * 工单当前状态
     */
    @TableField("order_status")
    private String orderStatus;
    
    /**
     * 工单处理时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("order_time")
    private Date orderTime;
    
    /**
     * 操作
     */
    @TableField("operation")
    private String operation;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    
    /**
     * 公司负责人审批意见
     */
    @TableField("opinion")
    private String opinion;
    /**
     * 整改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("fix_time")
    private Date fixTime;
}
