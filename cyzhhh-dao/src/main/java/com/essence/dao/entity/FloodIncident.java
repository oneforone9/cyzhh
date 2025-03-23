package com.essence.dao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 积水事件处置
 *
 * @author bird
 * @date 2025/03/20 16:06
 * @Description
 */
@Data
@TableName("flood_incident")
public class FloodIncident extends Model<FloodIncident> {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;

    /**
     * 详细位置描述
     */
    @TableField("detailed_location")
    private String detailedLocation;

    /**
     * 申请时间(排水中心)
     */
    @TableField("application_time")
    private Date applicationTime;

    /**
     * 申请用户主键
     */
    @TableField("application_user_id")
    private String applicationUserId;

    /**
     * 申请名称
     */
    @TableField("application_user_name")
    private String applicationUserName;

    /**
     * 申请电话
     */
    @TableField("application_phone")
    private String applicationPhone;

    /**
     * 审核时间（防御科）
     */
    @TableField("review_time")
    private Date reviewTime;

    /**
     * 下发时间（防御科）
     */
    @TableField("dispatch_time")
    private Date dispatchTime;

    /**
     * 提交时间
     */
    @TableField("submission_time")
    private Date submissionTime;

    /**
     * 预计到达时间
     */
    @TableField("estimated_arrival_time")
    private Date estimatedArrivalTime;

    /**
     * 实际到达时间
     */
    @TableField("actual_arrival_time")
    private Date actualArrivalTime;

    /**
     * 积水完成时间
     */
    @TableField("flood_mitigation_completion_time")
    private Date floodMitigationCompletionTime;

    /**
     * 状态：0-待审核，1-待响应，2-待处置，3-已归档，4-已终止
     */
    @TableField("status")
    private Integer status;

    /**
     * 标签：0-驳回，1-通过
     */
    @TableField("tag")
    private Integer tag;

    /**
     * 规划轨迹
     */
    @TableField("planned_track")
    private String plannedTrack;

    /**
     * 实际轨迹
     */
    @TableField("actual_track")
    private String actualTrack;

    /**
     * 抢险队伍主键
     */
    @TableField("group_id")
    private Integer groupId;

    /**
     * 抢险队伍名称
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 抢险队伍，组织机构ID
     */
    @TableField("corp_id")
    private String corpId;

    /**
     * 驳回原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 记录表主键
     */
    @TableField("record_id")
    private Long recordId;

    /**
     * 类型 0:排水中心申请 1：防御科申请
     */
    @TableField("flood_incident_type")
    private Integer floodIncidentType;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;
}
