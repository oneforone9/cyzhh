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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 方案执行进度表实体
 *
 * @author BINX
 * @since 2023-04-18 17:03:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_case_progress")
public class StCaseProgressDto extends Model<StCaseProgressDto> {

    private static final long serialVersionUID = -95336952320801467L;
        
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 方案id
     */
    @TableField("case_id")
    private String caseId;
    
    /**
     * 进度百分比
     */
    @TableField("progress")
    private BigDecimal progress;
    /**
     * 方案状态 1 未开始 2 进行中 3 计算失败 4 计算完成
     */
    @TableField("status")
    private String status;
    
    /**
     * 备注
     */
    @TableField("message")
    private String message;
    
    /**
     * 新增或者更新
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 方案名称
     */
    private String caseName;

    /**
     * 模型类型  1 防汛调度  2 水资源调度
     */
    private Integer modelType;

}
