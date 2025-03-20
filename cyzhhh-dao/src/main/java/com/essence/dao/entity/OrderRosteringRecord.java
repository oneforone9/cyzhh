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
 * 值班表生成工单记录表(避免重复生成工单)(OrderRosteringRecord)实体类
 *
 * @author zhy
 * @since 2022-10-31 17:53:17
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_order_rostering_record")
public class OrderRosteringRecord extends Model<OrderRosteringRecord> {

    private static final long serialVersionUID = 379330047242346491L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 值班表主键
     */
    @TableField("rostering_id")
    private String rosteringId;

    /**
     * 工单表主键
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 日期
     */
    @TableField("tm")
    private String tm;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

}
