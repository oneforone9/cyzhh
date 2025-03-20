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
 * 科室联系人表实体
 *
 * @author liwy
 * @since 2023-03-29 14:21:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_office_base_relation")
public class StOfficeBaseRelationDto extends Model<StOfficeBaseRelationDto> {

    private static final long serialVersionUID = 867946796342662915L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
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
     * 使用人id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 办公电话
     */
    @TableField("office_telephone")
    private String officeTelephone;
    
    /**
     * 职务
     */
    @TableField("job")
    private String job;
    
    /**
     * 移动电话
     */
    @TableField("phone_number")
    private String phoneNumber;
    
    /**
     * 房间号
     */
    @TableField("room_no")
    private String roomNo;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


    /**
     * 是否合同管理员 0:不是； 1:是
     */
    @TableField("is_ht_gly")
    private Integer isHtGly;
    
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
