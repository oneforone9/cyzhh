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
 * 设备巡检任务编码实体
 *
 * @author majunjie
 * @since 2025-01-10 13:35:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xjbh")
public class XjbhDto extends Model<XjbhDto> {

    private static final long serialVersionUID = 634229490585966805L;
    
    /**
     * 类型0摄像头1会议室2问题
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 编号
     */
    @TableField("bh")
    private Integer bh;

}
