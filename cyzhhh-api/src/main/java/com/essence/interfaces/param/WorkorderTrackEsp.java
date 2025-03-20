package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单巡查轨迹参数实体
 *
 * @author zhy
 * @since 2022-11-09 17:49:06
 */

@Data
public class WorkorderTrackEsp extends Esp {

    private static final long serialVersionUID = 392906287771651628L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 经度（小程序端上传火星坐标系)
     */
    @ColumnMean("lgtd")
    private Double lgtd;

    /**
     * 纬度（小程序端上传火星坐标系)
     */
    @ColumnMean("lttd")
    private Double lttd;

    /**
     * 转换之后的经度(pc展示84坐标系)
     */
    @ColumnMean("change_lgtd")
    private Double changeLgtd;

    /**
     * 转换之后的纬度(pc展示84坐标系)
     */
    @ColumnMean("change_lttd")
    private Double changeLttd;

    /**
     * 工单主键
     */
    @ColumnMean("order_id")
    private String orderId;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;

    /**
     * 速度
     */
    @ColumnMean("speed")
    private Double speed;


}
