package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author majunjie
 * @since 2024-09-11 14:21:34
 */

@Data
public class ViewOfficeBaseEsp extends Esp {

    private static final long serialVersionUID = 639194989774754146L;

        /**
     * 主键
     */        @ColumnMean("id")
    private Integer id;
    /**
     * 科室名
     */        @ColumnMean("dept_name")
    private String deptName;
    /**
     * 排序
     */        @ColumnMean("sort")
    private Integer sort;
    /**
     * 科室基础表ID（st_office_base.id）
     */        @ColumnMean("office_base_id")
    private Integer officeBaseId;
    /**
     * 使用人姓名
     */        @ColumnMean("user_name")
    private String userName;
    /**
     * 职务
     */        @ColumnMean("job")
    private String job;
    /**
     * 用户id
     */        @ColumnMean("user_id")
    private String userId;


}
