package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单巡查轨迹返回实体
 *
 * @author zhy
 * @since 2022-11-09 17:49:06
 */

@Data
public class WorkorderTrackEsr extends Esr {

    private static final long serialVersionUID = -97365954273345963L;


    /**
     * 主键
     */
    private String id;

    /**
     * 经度（小程序端上传火星坐标系)
     */
    private Double lgtd;

    /**
     * 纬度（小程序端上传火星坐标系)
     */
    private Double lttd;

    /**
     * 转换之后的经度(pc展示84坐标系)
     */
    private Double changeLgtd;

    /**
     * 转换之后的纬度(pc展示84坐标系)
     */
    private Double changeLttd;

    /**
     * 工单主键
     */
    private String orderId;

    /**
     * 新增时间
     */

    private Date gmtCreate;

    /**
     * 速度
     */
    private Double speed;

}
