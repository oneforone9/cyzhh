package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典表参数实体
 *
 * @author zhy
 * @since 2022-11-03 17:12:29
 */

@Data
public class SysDictionaryTypeEsp extends Esp {

    private static final long serialVersionUID = -99932466738804382L;


    /**
     * 字典类型ID
     */
    @ColumnMean("id")
    private String id;

    /**
     * 字典类型
     */
    @ColumnMean("dictionary_type")
    private String dictionaryType;

    /**
     * 字典类型名称
     */
    @ColumnMean("dictionary_type_name")
    private String dictionaryTypeName;

    /**
     * 显示顺序
     */
    @ColumnMean("sort")
    private Long sort;

    /**
     * 创建时间
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
     * 是否逻辑删除（0正常/未删除  1删除）
     */
    @ColumnMean("is_deleted")
    private String isDeleted;


}
