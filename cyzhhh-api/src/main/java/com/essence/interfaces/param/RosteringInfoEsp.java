package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 人员巡河排班信息表参数实体
 *
 * @author zhy
 * @since 2022-10-25 18:07:34
 */

@Data
public class RosteringInfoEsp extends Esp {

    private static final long serialVersionUID = 216835218228178749L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 人员主键
     */
    @ColumnMean("person_id")
    private String personId;

    /**
     * 人员名称
     */
    @ColumnMean("person_name")
    private String personName;

    /**
     * 河道主键
     */
    @ColumnMean("rea_id")
    private String reaId;

    /**
     * 河道名称
     */
    @ColumnMean("rea_name")
    private String reaName;

    /**
     * 部门主键
     */
    @ColumnMean("department_id")
    private String departmentId;

    /**
     * 部门名称
     */
    @ColumnMean("department_name")
    private String departmentName;

    /**
     * 单位主键
     */
    @ColumnMean("unit_id")
    private String unitId;

    /**
     * 单位名称
     */
    @ColumnMean("unit_name")
    private String unitName;
    /**
     * 重点巡查位置主键
     */
    @ColumnMean("focus_position_id")
    private String focusPositionId;
    /**
     * 重点巡查位置
     */
    @ColumnMean("focus_position")
    private String focusPosition;

    /**
     * 上班日期(1周一 2周二 3周三 4周四 5周五 6单周六 7单 周日 8双周六 9双周日 多个时间使用英文逗号分割)
     */
    @ColumnMean("work_time")
    private String workTime;

    /**
     * 是否删除(1是 0否)
     */
    @ColumnMean("is_delete")
    private String isDelete;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;

    /**
     * 更新者
     */
    @ColumnMean("updater")
    private String updater;

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;

    /**
     * 是否跨过节假日 1 是跨过
     */
    @ColumnMean("remarkw")
    private Integer crossw;
}
