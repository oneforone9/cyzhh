package com.essence.interfaces.dot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WorkMarkResDto implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 工单负责人
     */

    private String orderManager;

    /**
     * 工单类型(1巡查 2 保洁 3 绿化 4维保 5维修)
     */

    private String orderType;

    /**
     * 工单编号
     */

    private String orderCode;

    /**
     * 工单来源(1 计划生成 2 巡查上报)
     */

    private String orderSource;

    /**
     * 工单名称
     */

    private String orderName;

    /**
     * 派发方式(1人工派发 2自动派发)
     */

    private String distributeType;

    /**
     * 作业区域
     */

    private String location;
    /**
     * 事件主键
     */

    private String eventId;

    /**
     * 所属单位主键
     */

    private String unitId;

    /**
     * 所属单位名称
     */

    private String unitName;

    /**
     * 创建时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date startTime;

    /**
     * 处理时段（以分钟为单位）
     */

    private Integer timeSpan;

    /**
     * 截止时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date endTime;

    /**
     * 工单标签（1市级交办 2区级交办 3局交办）
     */

    private String orderLabel;

    /**
     * 工单描述
     */

    private String orderDesc;

    /**
     * 是否关注（1是 0否）
     */

    private String isAttention;

    /**
     * 经度
     */

    private Double lgtd;

    /**
     * 纬度
     */

    private Double lttd;

    /**
     * 是否删除（1是 0否）
     */

    private String isDelete;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date gmtModified;

    /**
     * 创建者
     */

    private String creator;

    /**
     * 更新者
     */

    private String updater;

    /**
     * 备注
     */

    private String remark;
    /**
     * 当前工单处理过程主键
     */

    private String recordId;

    /**
     * 工单当前状态
     */

    private String orderStatus;
    /**
     * 当前处理人主键
     */

    private String personId;
    /**
     * 当前处理人名称
     */

    private String personName;
    /**
     * 经办人主键集
     */

    private String operatorIds;
    /**
     * 重点河段 信息
     */
    private List<WorkorderRecordGeomDto> workorderRecordGeomDtos;
}
