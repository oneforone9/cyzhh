package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 人员部门关系表参数实体
 *
 * @author zhy
 * @since 2022-10-24 17:42:56
 */

@Data
public class RelPersonDepartmentEsp extends Esp {

    private static final long serialVersionUID = -62543748758524471L;


    /**
     * 人员主键
     */
    @ColumnMean("person_id")
    private String personId;

    /**
     * 部门主键
     */
    @ColumnMean("department_id")
    private String departmentId;


}
