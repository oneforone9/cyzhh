package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 人员巡河排班信息表更新实体
 *
 * @author zhy
 * @since 2022-10-25 11:22:52
 */

@Data
public class RosteringInfoEsu extends Esu {

    private static final long serialVersionUID = -20798038230017750L;


    /**
     * 主键
     *
     * @mock 1231
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;

    /**
     * 人员主键
     *
     * @mock 1
     */
    @Size(max = 32, message = "人员主键的最大长度:32")
    @NotEmpty(groups = Insert.class, message = "人员主键不能为空")
    private String personId;

    /**
     * 人员名称
     *
     * @mock 张三
     */
    @Size(max = 10, message = "姓名的最大长度:10")
    @NotEmpty(groups = Insert.class, message = "姓名不能为空")
    private String personName;

    /**
     * 河道主键
     *
     * @mock 1
     */
    @NotEmpty(groups = Insert.class, message = "河道主键不能为空")
    private String reaId;


    /**
     * 河道名称
     *
     * @mock 朝阳干渠
     */
    @Size(max = 50, message = "河道名称的最大长度:50")
    @NotEmpty(groups = Insert.class, message = "河道名称不能为空")
    private String reaName;
    /**
     * 部门主键
     *
     * @mock 1
     */
    @NotNull(groups = Insert.class, message = "部门主键不能为空")
    private String departmentId;

    /**
     * 部门名称
     * @mock 巡查一组
     */
    @Size(max = 20, message = "部门名称的最大长度:20")
    @NotEmpty(groups = Insert.class, message = "部门名称不能为空")
    private String departmentName;
    /**
     * 单位主键
     */
    @Size(max = 32, message = "单位主键的最大长度:32")
    @NotEmpty(groups = Insert.class, message = "单位主键不能为空")
    private String unitId;

    /**
     * 单位名称
     *
     * @mock 河道管理一所
     */
    @Size(max = 20, message = "单位名称的最大长度:20")
    @NotEmpty(groups = Insert.class, message = "单位名称不能为空")
    private String unitName;

    /**
     * 重点巡查位置主键
     *
     * @mock 1
     */
    @NotEmpty(groups = Insert.class, message = "重点巡查位置主键不能为空")
    private String focusPositionId;

    /**
     * 重点巡查位置
     *
     * @mock 全段
     */
    @Size(max = 100, message = "重点巡查位置的最大长度:400")
    @NotEmpty(groups = Insert.class, message = "重点巡查位置不能为空")
    private String focusPosition;

    /**
     * 上班日期(1周一 2周二 3周三 4周四 5周五 6单周六 7单 周日 8双周六 9双周日 多个时间使用英文逗号分割)
     *
     * @mock 1, 2, 3
     */
    @Size(max = 30, message = "上班日期的最大长度:30")
    @NotEmpty(groups = Insert.class, message = "上班日期不能为空")
    private String workTime;

    /**
     * 是否删除(1是 0否)
     *
     * @ignore
     */
    private String isDelete;

    /**
     * 新增时间(不传)
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间(不传)
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 创建者(不传)
     */
    private String creator;

    /**
     * 更新者(不传)
     */
    private String updater;

    /**
     * 备注
     *
     * @mock 备注
     */
    @Size(max = 1000, message = "备注的最大长度:1000")
    private String remark;

    /**
     * 是否跨过节假日 1 是跨过
     */
    private Integer crossw;
}
