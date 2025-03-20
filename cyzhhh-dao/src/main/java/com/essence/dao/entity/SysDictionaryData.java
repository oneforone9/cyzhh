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
 * 字典数据表(SysDictionaryData)实体类
 *
 * @author zhy
 * @since 2022-11-03 17:12:25
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_dictionary_data")
public class SysDictionaryData extends Model<SysDictionaryData> {

    private static final long serialVersionUID = -25457908358361362L;


    /**
     * 字典ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 字典名称
     */
    @TableField("dictionary_name")
    private String dictionaryName;

    /**
     * 字典值
     */
    @TableField("dictionary_value")
    private String dictionaryValue;

    /**
     * 字典类型
     */
    @TableField("dictionary_type")
    private String dictionaryType;

    /**
     * 显示顺序
     */
    @TableField("sort")
    private Long sort;

    /**
     * 父ID
     */
    @TableField("parent_id")
    private String parentId;

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


    /**
     * 处理单位（1-第三方公司 2-水环境组 3-闸坝设施组）
     */
    @TableField("deal_unit")
    private Integer dealUnit;
}
