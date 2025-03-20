package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 文库类型基础表参数实体
 *
 * @author liwy
 * @since 2023-08-17 10:31:36
 */

@Data
public class StLibraryTypeEsp extends Esp {

    private static final long serialVersionUID = 618775300403392790L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 文库类型
     */
    @ColumnMean("library_type")
    private String libraryType;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
    /**
     * 排序
     */
    @ColumnMean("sort")
    private Integer sort;


}
