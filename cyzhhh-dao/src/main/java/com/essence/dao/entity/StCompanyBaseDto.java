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
 * 第三方服务公司基础表实体
 *
 * @author BINX
 * @since 2023-02-16 11:58:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_company_base")
public class StCompanyBaseDto extends Model<StCompanyBaseDto> {

    private static final long serialVersionUID = 699992232717453275L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 公司名称
     */
    @TableField("company")
    private String company;
    
    /**
     * 服务年份
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("service_year")
    private Date serviceYear;
    
    /**
     * 负责人
     */
    @TableField("manage_name")
    private String manageName;
    
    /**
     * 负责人联系方式
     */
    @TableField("manage_phone")
    private String managePhone;
    
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
     * 备注
     */
    @TableField("remark")
    private String remark;
    
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
