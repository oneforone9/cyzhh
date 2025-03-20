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
 * 设备巡检流程实体
 *
 * @author majunjie
 * @since 2025-01-09 15:09:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xjrwlc")
public class XjrwlcDto extends Model<XjrwlcDto> {

    private static final long serialVersionUID = 489470414211217843L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 任务id
     */
    @TableField("rw_id")
    private String rwId;

    /**
     * 描述工单状态
     */
    @TableField("ms")
    private String ms;

    /**
     * 操作人
     */
    @TableField("czr")
    private String czr;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("cjsj")
    private Date cjsj;

}
