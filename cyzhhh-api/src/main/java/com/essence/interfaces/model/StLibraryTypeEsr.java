package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 文库类型基础表返回实体
 *
 * @author liwy
 * @since 2023-08-17 10:31:39
 */

@Data
public class StLibraryTypeEsr extends Esr {

    private static final long serialVersionUID = 308278783977799041L;

    private Integer id;


    /**
     * 文库类型
     */
    private String libraryType;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


    /**
     * 排序
     */
    private Integer sort;


}
