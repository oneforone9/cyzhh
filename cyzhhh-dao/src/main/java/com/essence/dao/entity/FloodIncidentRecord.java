package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 积水事件处置记录表
 *
 * @author bird
 * @date 2025/03/20 16:09
 * @Description
 */
@Data
@TableName("flood_incident_record")
public class FloodIncidentRecord {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 积水事件处置ID
     */
    @TableField("incident_id")
    private Long incidentId;

    /**
     * 之前状态
     */
    @TableField("previous_status")
    private Integer previousStatus;

    /**
     * 新状态
     */
    @TableField("new_status")
    private Integer newStatus;

    /**
     * 操作人ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 操作人name
     */
    @TableField("user_name")
    private String userName;

    /**
     * 组织机构ID
     */
    @TableField("corp_id")
    private String corpId;

    /**
     * 组织机构名称
     */
    @TableField("corp_name")
    private String corpName;

    /**
     * 变更时间
     */
    @TableField("change_time")
    private Date changeTime;


    /**
     * 操作 （申请、下发、审核、驳回  接受 拒绝 完成）
     */
    @TableField("operate")
    private String operate;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;
}