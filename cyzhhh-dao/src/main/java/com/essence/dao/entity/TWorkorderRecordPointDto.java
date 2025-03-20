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
 * 工单地理信息记录表-记录工单创建时的打卡点位信息实体
 *
 * @author liwy
 * @since 2023-05-07 12:12:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_workorder_record_point")
public class TWorkorderRecordPointDto extends Model<TWorkorderRecordPointDto> {

    private static final long serialVersionUID = 691262922020906274L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 工单主键
     */
    @TableField("order_id")
    private String orderId;
    
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

    /**
     * 是否在打卡点位完成打卡 0-未完成打卡  1-已完成打卡
     */
    @TableField("is_complete_clock")
    private Integer isCompleteClock;

}
