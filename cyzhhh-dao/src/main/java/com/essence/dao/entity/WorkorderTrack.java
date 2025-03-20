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
 * 工单巡查轨迹(WorkorderTrack)实体类
 *
 * @author zhy
 * @since 2022-11-09 17:49:06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_workorder_track")
public class WorkorderTrack extends Model<WorkorderTrack> {

    private static final long serialVersionUID = 950306790493895123L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 经度（小程序端上传火星坐标系)
     */
    @TableField("lgtd")
    private Double lgtd;

    /**
     * 纬度（小程序端上传火星坐标系)
     */
    @TableField("lttd")
    private Double lttd;

    /**
     * 转换之后的经度(pc展示84坐标系)
     */
    @TableField("change_lgtd")
    private Double changeLgtd;

    /**
     * 转换之后的纬度(pc展示84坐标系)
     */
    @TableField("change_lttd")
    private Double changeLttd;

    /**
     * 工单主键
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;


    /**
     * 速度
     */
    @TableField("speed")
    private Double speed;

}
