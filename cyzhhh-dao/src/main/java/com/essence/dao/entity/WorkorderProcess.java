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
 * 工单处理过程表(WorkorderProcess)实体类
 *
 * @author zhy
 * @since 2022-10-27 15:26:30
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_workorder_process")
public class WorkorderProcess extends Model<WorkorderProcess> {

    private static final long serialVersionUID = 346492941959696217L;


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
     * 操作描述
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
