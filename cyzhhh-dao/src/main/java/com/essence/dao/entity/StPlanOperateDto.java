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
 * 养护内容记录表实体
 *
 * @author liwy
 * @since 2023-07-24 14:17:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_plan_operate")
public class StPlanOperateDto extends Model<StPlanOperateDto> {

    private static final long serialVersionUID = 966763718125717412L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 工单主键(生成事件的工单主键)
     */
    @TableField("order_id")
    private String orderId;
    
    /**
     * 设备设施名称
     */
    @TableField("equipment_name")
    private String equipmentName;
    
    /**
     * 日常维护内容
     */
    @TableField("service_content")
    private String serviceContent;
    
    /**
     * 维护前图片
     */
    @TableField("yh_before")
    private String yhBefore;
    
    /**
     * 维护后图片
     */
    @TableField("yh_after")
    private String yhAfter;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

}
