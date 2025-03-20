package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 河道部门关系表参数实体
 *
 * @author zhy
 * @since 2022-10-24 17:43:25
 */

@Data
public class RelReaDepartmentEsp extends Esp {

    private static final long serialVersionUID = 704029343735120370L;


    /**
     * 河道主键
     */
    @ColumnMean("rea_id")
    private String reaId;

    /**
     * 部门主键
     */
    @ColumnMean("department_id")
    private String departmentId;

}
