package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 河道部门关系表(RelReaDepartment)实体类
 *
 * @author zhy
 * @since 2022-10-24 17:44:00
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "rel_rea_department")
public class RelReaDepartment extends Model<RelReaDepartment> {

    private static final long serialVersionUID = -41167119028329640L;


    /**
     * 河道主键
     */
    @TableField(value = "rea_id")
    private String reaId;

    /**
     * 部门主键
     */
    @TableField("department_id")
    private String departmentId;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;

}
