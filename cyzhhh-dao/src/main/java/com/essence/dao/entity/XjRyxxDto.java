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
 * 设备巡检人员信息实体
 *
 * @author majunjie
 * @since 2025-01-08 08:14:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xj_ryxx")
public class XjRyxxDto extends Model<XjRyxxDto> {

    private static final long serialVersionUID = 120700534887023407L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 联系方式
     */
    @TableField("lxfs")
    private String lxfs;

    /**
     * 所在班组
     */
    @TableField("bzmc")
    private String bzmc;

    /**
     * 所在班组id
     */
    @TableField("bz_id")
    private String bzId;

    /**
     * 生成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;

    /**
     * 状态0启用1停用
     */
    @TableField("type")
    private Integer type;
    /**
     * 类型0成员1班组长
     */
    @TableField("lx")
    private Integer lx;
    /**
     * 部门id
     */
    @TableField("bmid")
    private String bmid;
}
