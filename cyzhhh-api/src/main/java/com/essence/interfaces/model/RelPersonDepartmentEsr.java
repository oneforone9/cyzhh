package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 人员部门关系表返回实体
 *
 * @author zhy
 * @since 2022-10-24 17:42:56
 */

@Data
public class RelPersonDepartmentEsr extends Esr {

    private static final long serialVersionUID = -91485892625119924L;


    /**
     * 人员主键
     */
    private String personId;

    /**
     * 部门主键
     */
    private String departmentId;

    /**
     * 人员姓名
     */
    private String personName;


}
