package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 人员巡河排班信息表返回实体
 *
 * @author zhy
 * @since 2022-10-25 18:07:33
 */

@Data
public class RosteringInfoEsr extends Esr {

    private static final long serialVersionUID = -18483135032879514L;


    /**
     * 主键
     */
    private String id;

    /**
     * 人员主键
     */
    private String personId;

    /**
     * 人员名称
     */
    private String personName;

    /**
     * 河道主键
     */
    private String reaId;

    /**
     * 河道名称
     */
    private String reaName;

    /**
     * 部门主键
     */
    private String departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 单位主键
     */
    private String unitId;

    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 重点巡查位置主键
     */
    private String focusPositionId;
    /**
     * 重点巡查位置
     */
    private String focusPosition;

    /**
     * 上班日期(1周一 2周二 3周三 4周四 5周五 6单周六 7单 周日 8双周六 9双周日 多个时间使用英文逗号分割)
     */
    private String workTime;

    /**
     * 是否删除(1是 0否)
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
     * 是否跨过节假日 1 是跨过
     */
    private Integer crossw;
}
