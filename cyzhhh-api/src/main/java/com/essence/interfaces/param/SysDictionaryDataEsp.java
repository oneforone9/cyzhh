package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典数据表参数实体
 *
 * @author zhy
 * @since 2022-11-03 17:12:26
 */

@Data
public class SysDictionaryDataEsp extends Esp {

    private static final long serialVersionUID = 843971046074567257L;


    /**
     * 字典ID
     */
    @ColumnMean("id")
    private String id;

    /**
     * 字典名称
     */
    @ColumnMean("dictionary_name")
    private String dictionaryName;

    /**
     * 字典值
     */
    @ColumnMean("dictionary_value")
    private String dictionaryValue;

    /**
     * 字典类型
     */
    @ColumnMean("dictionary_type")
    private String dictionaryType;

    /**
     * 显示顺序
     */
    @ColumnMean("sort")
    private Long sort;

    /**
     * 父ID
     */
    @ColumnMean("parent_id")
    private String parentId;

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
