package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 部门信息表参数实体
 *
 * @author zhy
 * @since 2022-10-24 16:50:28
 */

@Data
public class DepartmentBaseEsp extends Esp {

    private static final long serialVersionUID = 401722162823590748L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 名称
     */
    @ColumnMean("department_name")
    private String departmentName;

    /**
     * 单位主键
     */
    @ColumnMean("unit_id")
    private String unitId;

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
     * 班长ID（人员基础信息表主键）
     */
    @ColumnMean("remark")
    private String personBaseId;

    /**
     * 姓名
     */
    @ColumnMean("name")
    private String name;


}
