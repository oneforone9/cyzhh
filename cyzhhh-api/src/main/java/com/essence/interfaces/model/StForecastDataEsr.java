package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预警报表记录返回实体
 *
 * @author majunjie
 * @since 2023-04-17 19:39:03
 */

@Data
public class StForecastDataEsr extends Esr {

    private static final long serialVersionUID = 399668737961327506L;


    /**
     * 主键
     */
    private String id;


    /**
     * 主键
     */
    private String forecastId;


    /**
     * 站点名称
     */
    private String stnm;


    /**
     * 所属河道
     */
    private String rvnm;


    /**
     * 预警类型
     */
    private String forecastType;


    /**
     * 原因描述
     */
    private String reason;


    /**
     * 预警状态0待处理1处理中2已处理
     */
    private Integer forecastState;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

}
