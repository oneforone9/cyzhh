package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
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
public class FloodIncidentRecordEsu extends Esu {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 积水事件处置ID
     */
    private Long incidentId;

    /**
     * 之前状态
     */
    private Integer previousStatus;

    /**
     * 新状态
     */
    private Integer newStatus;

    /**
     * 操作人ID
     */
    private String userId;

    /**
     * 组织机构ID
     */
    private String corpId;

    /**
     * 变更时间
     */
    private Date changeTime;

    /**
     * 备注信息
     */
    private String remarks;
}