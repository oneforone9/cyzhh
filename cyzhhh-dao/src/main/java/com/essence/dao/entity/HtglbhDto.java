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
 * 实体
 *
 * @author majunjie
 * @since 2024-09-10 14:56:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "htglbh")
public class HtglbhDto extends Model<HtglbhDto> {

    private static final long serialVersionUID = 175597010960044564L;
    
    /**
     * 类型BF/YF/GF/HC/SG/DL/CQ/QT
     */
    @TableId(value = "lx", type = IdType.INPUT)
    private String lx;

    /**
     * 序号
     */
    @TableField("xh")
    private Integer xh;

}
