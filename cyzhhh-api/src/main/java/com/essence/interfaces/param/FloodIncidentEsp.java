package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
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
public class FloodIncidentEsp extends Esp {

    /**
     * 主键ID
     */
    @ColumnMean("id")
    private Long id;

    /**
     * 纬度
     */
    @ColumnMean("latitude")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @ColumnMean("longitude")
    private BigDecimal longitude;

    /**
     * 详细位置描述
     */
    @ColumnMean("detailed_location")
    private String detailedLocation;

    /**
     * 申请时间(排水中心)
     */
    @ColumnMean("application_time")
    private Date applicationTime;

    /**
     * 申请用户主键
     */
    @ColumnMean("application_user_id")
    private String applicationUserId;

    /**
     * 申请名称
     */
    @ColumnMean("application_user_name")
    private String applicationUserName;

    /**
     * 申请电话
     */
    @ColumnMean("application_phone")
    private String applicationPhone;

    /**
     * 审核时间(防御科）
     */
    @ColumnMean("review_time")
    private Date reviewTime;

    /**
     * 下发时间(防御科）
     */
    @ColumnMean("dispatch_time")
    private Date dispatchTime;

    /**
     * 提交时间
     */
    @ColumnMean("submission_time")
    private Date submissionTime;

    /**
     * 预计到达时间
     */
    @ColumnMean("estimated_arrival_time")
    private Date estimatedArrivalTime;

    /**
     * 实际到达时间
     */
    @ColumnMean("actual_arrival_time")
    private Date actualArrivalTime;

    /**
     * 积水完成时间
     */
    @ColumnMean("flood_mitigation_completion_time")
    private Date floodMitigationCompletionTime;

    /**
     * 状态：0-待审核，1-待响应，2-待处置，3-已归档，4-已终止
     */
    @ColumnMean("status")
    private Integer status;

    /**
     * 标签：0-驳回，1-通过
     */
    @ColumnMean("tag")
    private Integer tag;

    /**
     * 规划轨迹
     */
    @ColumnMean("planned_track")
    private String plannedTrack;

    /**
     * 实际轨迹
     */
    @ColumnMean("actual_track")
    private String actualTrack;

    /**
     * 抢险队伍主键
     */
    @ColumnMean("group_id")
    private Integer groupId;

    /**
     * 抢险队伍名称
     */
    @ColumnMean("group_name")
    private String groupName;

    /**
     * 抢险队伍，组织机构ID
     */
    @ColumnMean("corp_id")
    private String corpId;

    /**
     * 驳回原因
     */
    @ColumnMean("reason")
    private String reason;

    /**
     * 记录表主键
     */
    @ColumnMean("record_id")
    private Long recordId;

    /**
     * 类型 0:排水中心申请 1：防御科申请
     */
    @ColumnMean("flood_incident_type")
    private Integer floodIncidentType;

    /**
     * 创建时间
     */
    @ColumnMean( "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @ColumnMean( "updated_time")
    private Date updatedTime;
}
