package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 部门信息表更新实体
 *
 * @author zhy
 * @since 2022-10-24 16:50:28
 */

@Data
public class DepartmentBaseEsu extends Esu {

    private static final long serialVersionUID = 158481939440386889L;


    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String departmentName;

    /**
     * 单位主键
     */
    private String unitId;

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
     * 班长ID（人员基础信息表主键）
     */
    private String personBaseId;

    /**
     * 姓名
     */
    private String name;


}
