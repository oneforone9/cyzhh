package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预警报表参数实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:28
 */

@Data
public class StForecastEsp extends Esp {

    private static final long serialVersionUID = 829993950149070619L;

    /**
     * 主键
     */
    @ColumnMean("forecast_id")
    private String forecastId;
    /**
     * 预警类型预警类型   水位站：1.水位超警戒17设备离线
     * 流量站 17设备离线
     * 闸坝 3阀门超载4阀门欠闸5左荷重超载6右荷重超载7PLC开度超限8PLC超载报警9电压异常10电流异常17设备离线
     * 泵站11设备故障12流量超上限13压力超上限14液位超上限15电能异常16格栅异常17设备离线
     */
    @ColumnMean("forecast_type")
    private String forecastType;
    /**
     * 站点编号
     */
    @ColumnMean("stcd")
    private String stcd;
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
     * 预警来源
     */
    @ColumnMean("source_type")
    private String sourceType;
    /**
     * 现场负责人
     */
    @ColumnMean("xcfzr")
    private String xcfzr;
    /**
     * 现场负责人联系方式
     */
    @ColumnMean("xcfzr_phone")
    private String xcfzrPhone;

    /**
     * 现场负责人需要和用户ID
     */
    @ColumnMean("xcfzr_user_id")
    private String xcfzrUserId;
    /**
     * 报警时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("police_time")
    private Date policeTime;
    /**
     * 预警状态0待处理1处理中2已处理
     */
    @ColumnMean("forecast_state")
    private Integer forecastState;
    /**
     * 设备类型1闸坝2泵站3水位站4流量站
     */
    @ColumnMean("device_type")
    private String deviceType;

    /**
     * 原因描述
     */
    @ColumnMean("reason")
    private String reason;
    /**
     * 设备主键（闸坝才有）
     */
    @ColumnMean("stcd_id")
    private Integer stcdId;
    /**
     * 河道编号
     */
    @ColumnMean("river")
    private String river;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @ColumnMean("lgtd")
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @ColumnMean("lttd")
    private Double lttd;
    /**
     * '处理人'
     */
    @ColumnMean("dispose")
    private String dispose;
    /**
     * '处理人联系方式'
     */
    @ColumnMean("dispose_phone")
    private String disposePhone;
    /**
     * 发送状态0未发送1发送
     */
    @ColumnMean("state")
    private Integer state;
    /**
     * 接收状态0未接收1已接收
     */
    @ColumnMean("reception")
    private Integer reception;
}
