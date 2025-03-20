package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典表(SysDictionaryType)实体类
 *
 * @author zhy
 * @since 2022-11-03 17:12:28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_dictionary_type")
public class SysDictionaryType extends Model<SysDictionaryType> {

    private static final long serialVersionUID = 982018922999594449L;


    /**
     * 字典类型ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 字典类型
     */
    @TableField("dictionary_type")
    private String dictionaryType;

    /**
     * 字典类型名称
     */
    @TableField("dictionary_type_name")
    private String dictionaryTypeName;

    /**
     * 显示顺序
     */
    @TableField("sort")
    private Long sort;

    /**
     * 创建时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;

    /**
     * 更新者
     */
    @TableField("updater")
    private String updater;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否逻辑删除（0正常/未删除  1删除）
     */
    @TableField("is_deleted")
    private String isDeleted;

}
