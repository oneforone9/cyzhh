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
 * 合同管理收藏实体
 *
 * @author majunjie
 * @since 2024-09-13 16:18:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "htglsc")
public class HtglscDto extends Model<HtglscDto> {

    private static final long serialVersionUID = 599030889275835938L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 合同编号
     */
    @TableField("htid")
    private String htid;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

}
