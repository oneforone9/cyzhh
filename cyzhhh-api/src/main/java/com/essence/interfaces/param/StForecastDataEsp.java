package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预警报表记录参数实体
 *
 * @author majunjie
 * @since 2023-04-17 19:39:02
 */

@Data
public class StForecastDataEsp extends Esp {

    private static final long serialVersionUID = -66075845759577325L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 主键
     */
    @ColumnMean("forecast_id")
    private String forecastId;
    /**
     * 站点名称
     */
    @ColumnMean("stnm")
    private String stnm;
    /**
     * 所属河道
     */
    @ColumnMean("rvnm")
    private String rvnm;
    /**
     * 预警类型
     */
    @ColumnMean("forecast_type")
    private String forecastType;
    /**
     * 原因描述
     */
    @ColumnMean("reason")
    private String reason;
    /**
     * 预警状态0待处理1处理中2已处理
     */
    @ColumnMean("forecast_state")
    private Integer forecastState;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("time")
    private Date time;

}
