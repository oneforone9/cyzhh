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
 * 三方养护人员信息表实体
 *
 * @author liwy
 * @since 2023-07-17 14:53:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_plan_person")
public class StPlanPersonDto extends Model<StPlanPersonDto> {

    private static final long serialVersionUID = -69290558255168239L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 养护人员id
     */
    @TableField("person_id")
    private String personId;
    
    /**
     * 养护人员
     */
    @TableField("plan_person")
    private String planPerson;
    
    /**
     * 联系方式
     */
    @TableField("plan_phone")
    private String planPhone;
    
    /**
     * 第三方服务公司主键id
     */
    @TableField("company_id")
    private String companyId;
    
    /**
     * 公司名称
     */
    @TableField("company")
    private String company;
    
    /**
     * 所属单位主键
     */
    @TableField("unit_id")
    private String unitId;
    
    /**
     * 所属单位名称
     */
    @TableField("unit_name")
    private String unitName;
    
    /**
     * 三方公司负责人
     */
    @TableField("name")
    private String name;
    
    /**
     * 三方公司负责人id
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 三方公司负责人手机号
     */
    @TableField("phone")
    private String phone;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 状态(0启用 1停用)
     */
    @TableField("status")
    private String status;

}
