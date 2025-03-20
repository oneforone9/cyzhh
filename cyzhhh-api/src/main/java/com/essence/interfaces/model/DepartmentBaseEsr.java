package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 部门信息表返回实体
 *
 * @author zhy
 * @since 2022-10-24 16:50:28
 */

@Data
public class DepartmentBaseEsr extends Esr {

    private static final long serialVersionUID = -16321782973212400L;


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
