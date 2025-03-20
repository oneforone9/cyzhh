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
 * 地下水埋深实体
 *
 * @author BINX
 * @since 2023-01-04 14:46:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_deep_water")
public class TDeepWaterDto extends Model<TDeepWaterDto> {

    private static final long serialVersionUID = -37281153720293965L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 年
     */
    @TableField("year")
    private String year;
    
    /**
     * 埋 信息
     */
    @TableField("deep_info")
    private String deepInfo;

    /**
     * 深度
     */
    @TableField("deep")
    private String deep;

    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 1 年度 2 月度
     */
    private String type;
}
