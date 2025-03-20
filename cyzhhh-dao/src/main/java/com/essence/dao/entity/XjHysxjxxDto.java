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
 * 设备巡检会议室关联巡检信息实体
 *
 * @author majunjie
 * @since 2025-01-09 14:00:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xj_hysxjxx")
public class XjHysxjxxDto extends Model<XjHysxjxxDto> {

    private static final long serialVersionUID = 976695947269743853L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 会议室id
     */
    @TableField("hys_id")
    private String hysId;

    /**
     * 会议室巡检项目
     */
    @TableField("hys_xjxm")
    private String hysXjxm;

    /**
     * 排序
     */
    @TableField("px")
    private Integer px;

}
