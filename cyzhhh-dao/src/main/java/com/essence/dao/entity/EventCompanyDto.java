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
 * 第三方服务公司人员配置实体
 *
 * @author majunjie
 * @since 2023-06-05 11:24:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_event_company")
public class EventCompanyDto extends Model<EventCompanyDto> {

    private static final long serialVersionUID = -77683221841629558L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
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
     * 服务年份
     */
    @TableField("service_year")
    private String serviceYear;
    
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
     * 数据类型（1-河道绿化保洁  2-闸坝运行养护）
     */
    @TableField("type")
    private Integer type;
    
    /**
     * 河段id
     */
    @TableField("river_id")
    private String riverId;
    
    /**
     * 河名称
     */
    @TableField("rvnm")
    private String rvnm;
    
    /**
     * 处理人
     */
    @TableField("name")
    private String name;
    
    /**
     * 处理人id
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 处理人手机号
     */
    @TableField("phone")
    private String phone;
    
    /**
     * 是否负责人0否1是
     */
    @TableField("f_type")
    private Integer fType;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

}
