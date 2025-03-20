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
 * 绿化保洁工作日志上报表-关联表实体
 *
 * @author liwy
 * @since 2023-03-17 17:20:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_green_report_relation")
public class StGreenReportRelationDto extends Model<StGreenReportRelationDto> {

    private static final long serialVersionUID = 535174746113014831L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 绿化保洁工作日志上报表st_green_report的id
     */
    @TableField("st_green_id")
    private String stGreenId;
    
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
