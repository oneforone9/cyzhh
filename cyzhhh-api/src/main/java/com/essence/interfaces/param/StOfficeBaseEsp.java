package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 科室基础表参数实体
 *
 * @author liwy
 * @since 2023-03-29 14:21:08
 */

@Data
public class StOfficeBaseEsp extends Esp {

    private static final long serialVersionUID = 451269930482160461L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 科室名
     */
    @ColumnMean("dept_name")
    private String deptName;
    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 类别 1-朝阳区水务局 2 -日常服务
     */
    @ColumnMean("type")
    private Integer type;
    /**
     * 排序
     */
    @ColumnMean("sort")
    private Integer sort;


}
