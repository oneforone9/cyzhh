package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 设备巡检摄像头巡检计划日期实体
 *
 * @author majunjie
 * @since 2025-01-08 17:51:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xj_sxt_jh_time")
public class XjSxtJhTimeDto extends Model<XjSxtJhTimeDto> {

    private static final long serialVersionUID = 155460174034662032L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 周计划时间id
     */
    @TableField("zjh_id")
    private String zjhId;
    /**
     * 计划id
     */
    @TableField("jh_id")
    private String jhId;

}
