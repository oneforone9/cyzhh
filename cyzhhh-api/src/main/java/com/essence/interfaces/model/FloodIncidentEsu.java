package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class FloodIncidentEsu extends Esu {

    /**
     * 主键ID
     */
    @NotNull(groups = Update.class, message = "主键不能为空")
    private Long id;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 详细位置描述
     */
    private String detailedLocation;

    /**
     * 申请时间(排水中心)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applicationTime;

    /**
     * 申请用户主键
     */
    private String applicationUserId;

    /**
     * 申请名称
     */
    private String applicationUserName;

    /**
     * 申请电话
     */
    private String applicationPhone;

    /**
     * 审核时间（防御科）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime;

    /**
     * 下发时间（防御科）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dispatchTime;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submissionTime;

    /**
     * 预计到达时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date estimatedArrivalTime;

    /**
     * 实际到达时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualArrivalTime;

    /**
     * 积水完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date floodMitigationCompletionTime;

    /**
     * 状态：0-待审核，1-待响应，2-待处置，3-已归档，4-已终止
     */
    private Integer status;

    /**
     * 标签：0-驳回，1-通过
     */
    private Integer tag;

    /**
     * 规划轨迹
     */
    private String plannedTrack;

    /**
     * 实际轨迹
     */
    private String actualTrack;

    /**
     * 抢险队伍主键
     */
    private Integer groupId;

    /**
     * 抢险队伍名称
     */
    private String groupName;

    /**
     * 抢险队伍，组织机构ID
     */
    @NotEmpty(groups = Insert.class, message = "抢险队伍，组织机构ID不能为空")
    private String corpId;

    /**
     * 驳回原因
     */
    private String reason;

    /**
     * 记录表主键
     */
    private Long recordId;

    /**
     * 类型 0:排水中心申请 1：防御科申请
     */
    @NotNull(groups = Insert.class, message = "类型不能为空")
    private Integer floodIncidentType;

    private String operateUserId;

    private String operateUserName;

    private String operateUserCorpId;

    private String operateUserCorpName;
}