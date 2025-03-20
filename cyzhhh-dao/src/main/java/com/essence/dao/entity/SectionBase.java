package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 断面基础档案信息
 *
 * @author bird
 * @date 2022/11/19 17:56
 * @Description
 */
@Data
@TableName("section_base")
public class SectionBase implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 断面名称
     */
    @TableField("section_name")
    private String sectionName;

    /**
     * 断面类型
     */
    @TableField("section_type")
    private Integer sectionType;


    /**
     * 新增时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;
}
