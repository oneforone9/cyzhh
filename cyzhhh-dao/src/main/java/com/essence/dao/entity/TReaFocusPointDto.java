package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 实体
 *
 * @author liwy
 * @since 2023-05-06 10:02:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_rea_focus_point")
public class TReaFocusPointDto extends Model<TReaFocusPointDto> {

    private static final long serialVersionUID = -54206453763005096L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 河道重点位置地理信息表主键id
     */
    @TableField("focus_id")
    private String focusId;
    
    /**
     * 打卡点位名称
     */
    @TableField("point_name")
    private String pointName;
    
    /**
     * 点位-经度
     */
    @TableField("lgtd")
    private BigDecimal lgtd;
    
    /**
     * 点位-纬度
     */
    @TableField("lttd")
    private BigDecimal lttd;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
