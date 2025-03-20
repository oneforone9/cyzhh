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
 * 闸坝计划生成记录表实体
 *
 * @author liwy
 * @since 2023-07-18 11:16:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_plan_record")
public class StPlanRecordDto extends Model<StPlanRecordDto> {

    private static final long serialVersionUID = -92819237861465652L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 闸坝计划排班信息表id
     */
    @TableField("plan_id")
    private Integer planId;
    
    /**
     * 维护日期排班表已按计划生成的标记
     */
    @TableField("wh_time")
    private String whTime;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

}
