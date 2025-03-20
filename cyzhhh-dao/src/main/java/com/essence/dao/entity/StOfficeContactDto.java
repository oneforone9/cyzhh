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
 * 实体
 *
 * @author liwy
 * @since 2023-03-29 18:52:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_office_contact")
public class StOfficeContactDto extends Model<StOfficeContactDto> {

    private static final long serialVersionUID = -35052484923160930L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 科室联系人表主键
     */
    @TableField("office_base_relation_id")
    private Integer officeBaseRelationId;
    
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

}
