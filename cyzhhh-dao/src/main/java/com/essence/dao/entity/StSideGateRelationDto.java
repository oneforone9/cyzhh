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
 * 闸坝负责人关联表实体
 *
 * @author liwy
 * @since 2023-04-13 17:51:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_side_gate_relation")
public class StSideGateRelationDto extends Model<StSideGateRelationDto> {

    private static final long serialVersionUID = -42640611109986657L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 边闸基础表主键id
     */
    @TableField("side_gate_id")
    private Integer sideGateId;
    
    /**
     * 行政负责人
     */
    @TableField("xzfzr")
    private String xzfzr;
    
    /**
     * 行政负责人联系方式
     */
    @TableField("xzfzr_phone")
    private String xzfzrPhone;
    
    /**
     * 现场负责人
     */
    @TableField("xcfzr")
    private String xcfzr;
    
    /**
     * 现场负责人联系方式
     */
    @TableField("xcfzr_phone")
    private String xcfzrPhone;
    
    /**
     * 现场负责人需要和用户ID
     */
    @TableField("xcfzr_user_id")
    private String xcfzrUserId;
    
    /**
     * 技术负责人
     */
    @TableField("jsfzr")
    private String jsfzr;
    
    /**
     * 技术负责人联系方式
     */
    @TableField("jsfzr_phone")
    private String jsfzrPhone;
    
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
    
    /**
     * 更新者
     */
    @TableField("updater")
    private String updater;
    
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;

}
