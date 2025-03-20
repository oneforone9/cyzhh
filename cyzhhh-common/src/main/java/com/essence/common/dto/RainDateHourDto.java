package com.essence.common.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 雨量站 小时雨量数据
 */
@Data
public class RainDateHourDto implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 站点名称
     */
    private String stnm;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private BigDecimal lttd;

    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private BigDecimal lgtd;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;

    /**
     * 场站id
     */
    private String stationID;

    /**
     * 小时雨量
     */
    private String rainh;

    /**
     * 1小时雨量
     */
    private String rain1h;

    /**
     * 3小时雨量
     */
    private String rain3h;

    /**
     * 6小时雨量
     */
    private String rain6h;

    /**
     * 12小时雨量
     */
    private String rain12h;

    /**
     * 24小时雨量
     */
    private String rain24h;

    /**
     * 用于雨量等值面计算
     */
    private Double rain;

}
