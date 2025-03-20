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
 * 人员巡河排班信息表(RosteringInfo)实体类
 *
 * @author zhy
 * @since 2022-10-25 18:07:32
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_rostering_info")
public class RosteringInfo extends Model<RosteringInfo> {

    private static final long serialVersionUID = 586284621870160071L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 人员主键
     */
    @TableField("person_id")
    private String personId;

    /**
     * 人员名称
     */
    @TableField("person_name")
    private String personName;

    /**
     * 河道主键
     */
    @TableField("rea_id")
    private String reaId;

    /**
     * 河道名称
     */
    @TableField("rea_name")
    private String reaName;

    /**
     * 部门主键
     */
    @TableField("department_id")
    private String departmentId;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 单位主键
     */
    @TableField("unit_id")
    private String unitId;

    /**
     * 单位名称
     */
    @TableField("unit_name")
    private String unitName;
    /**
     * 重点巡查位置主键
     */
    @TableField("focus_position_id")
    private String focusPositionId;

    /**
     * 重点巡查位置
     */
    @TableField("focus_position")
    private String focusPosition;

    /**
     * 上班日期(1周一 2周二 3周三 4周四 5周五 6单周六 7单 周日 8双周六 9双周日 多个时间使用英文逗号分割)
     */
    @TableField("work_time")
    private String workTime;

    /**
     * 是否删除(1是 0否)
     */
    @TableField("is_delete")
    private String isDelete;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;

    /**
     * 更新者
     */
    @TableField("updater")
    private String updater;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 是否跨过节假日 1 是跨过
     */
    private Integer crossw;

}
