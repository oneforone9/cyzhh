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
 * 设备巡检班组信息实体
 *
 * @author majunjie
 * @since 2025-01-08 08:13:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xj_bzxx")
public class XjBzxxDto extends Model<XjBzxxDto> {

    private static final long serialVersionUID = -27957982725978124L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 班组名称
     */
    @TableField("bzmc")
    private String bzmc;

    /**
     * 生成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;

    /**
     * 部门id
     */
    @TableField("bmid")
    private String bmid;

    /**
     * 会议室名称
     */
    @TableField("hy_name")
    private String hyName;

    /**
     * 会议室id
     */
    @TableField("hy_id")
    private String hyId;

}
