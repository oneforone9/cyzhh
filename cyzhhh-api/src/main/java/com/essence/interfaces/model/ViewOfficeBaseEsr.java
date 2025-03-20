package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2024-09-11 14:21:35
 */

@Data
public class ViewOfficeBaseEsr extends Esr {

    private static final long serialVersionUID = 581901118575998791L;


    /**
     * 主键
     */
    private String id;


    /**
     * 科室名
     */
    private String deptName;


    /**
     * 排序
     */
    private String sort;


    /**
     * 科室基础表ID（st_office_base.id）
     */
    private String officeBaseId;


    /**
     * 使用人姓名
     */
    private String userName;


    /**
     * 职务
     */
    private String job;


    /**
     * 用户id
     */
    private String userId;

    /**
     * 科室全称
     */
    private String departmentName;

}
