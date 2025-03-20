package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 实体
 *
 * @author majunjie
 * @since 2024-09-11 14:21:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "view_office_base")
public class ViewOfficeBaseDto extends Model<ViewOfficeBaseDto> {

    private static final long serialVersionUID = -49328038021829486L;

    /**
     * 主键
     */
    @TableField("id")
    private Integer id;
    /**
     * 科室名
     */
    @TableField("dept_name")
    private String deptName;
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * 科室基础表ID（st_office_base.id）
     */
    @TableField("office_base_id")
    private Integer officeBaseId;
    /**
     * 使用人姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 职务
     */
    @TableField("job")
    private String job;
    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;


    /**
     * 是否合同管理员 0 否 1 是
     */
    @TableField("is_ht_gly")
    private Integer isHtGly;


    /**
     * 是否局机关 0 否 1 是
     */
    @TableField("sf_jjg")
    private Integer sfJjg;

    /**
     * 科室全称
     */
    @TableField("department_name")
    private String departmentName;
}
