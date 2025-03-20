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
 * 闸坝运行维保日志上报-关联表实体
 *
 * @author liwy
 * @since 2023-03-15 11:57:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_gatedam_report_relation")
public class StGatedamReportRelationDto extends Model<StGatedamReportRelationDto> {

    private static final long serialVersionUID = 833513866513293454L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 闸坝运行维保日志上报表st_gatedam_report的id
     */
    @TableField("st_gatedam_id")
    private String stGatedamId;
    
    /**
     * 作业内容标记
     */
    @TableField("work_flag")
    private String workFlag;
    
    /**
     * 作业内容描述
     */
    @TableField("work_content")
    private String workContent;

    /**
     * 解决措施
     */
    @TableField("handle_way")
    private String handleWay;
    
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
