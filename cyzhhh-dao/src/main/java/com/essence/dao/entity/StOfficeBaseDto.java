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
 * 科室基础表实体
 *
 * @author liwy
 * @since 2023-03-29 14:21:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_office_base")
public class StOfficeBaseDto extends Model<StOfficeBaseDto> {

    private static final long serialVersionUID = -93019009353503185L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 科室名
     */
    @TableField("dept_name")
    private String deptName;

    /**
     * 科室名称（全称）
     */
    @TableField("department_name")
    private String departmentName;
    
    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    
    /**
     * 类别 1-朝阳区水务局 2 -日常服务
     */
    @TableField("type")
    private Integer type;
    
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 是否局机关 0 否 1 是
     */
    @TableField("sf_jjg")
    private Integer sfJjg;

}
