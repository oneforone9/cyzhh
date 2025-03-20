package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 预警报表返回实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:30
 */

@Data
public class StForecastEsr extends Esr {

    private static final long serialVersionUID = -91911998170639620L;


    /**
     * 主键
     */
    private String forecastId;


    /**
     * 预警类型预警类型   水位站：1.水位超警戒17设备离线
     * 流量站 17设备离线
     * 闸坝 3阀门超载4阀门欠闸5左荷重超载6右荷重超载7PLC开度超限8PLC超载报警9电压异常10电流异常17设备离线
     * 泵站11设备故障12流量超上限13压力超上限14液位超上限15电能异常16格栅异常17设备离线
     */
    private String forecastType;


    /**
     * 站点编号
     */
    private String stcd;


    /**
     * 站点名称
     */
    private String stnm;


    /**
     * 所属河道
     */
    private String rvnm;


    /**
     * 预警来源
     */
    private String sourceType;


    /**
     * 现场负责人
     */
    private String xcfzr;


    /**
     * 现场负责人联系方式
     */
    private String xcfzrPhone;
    /**
     * 现场负责人需要和用户ID
     */
    private String xcfzrUserId;

    /**
     * 报警时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date policeTime;


    /**
     * 预警状态0待处理1处理中2已处理
     */
    private Integer forecastState;
    /**
     * 设备类型1闸坝2泵站3水位站4流量站
     */
    private String deviceType;
    /**
     * 原因描述
     */
    private String reason;
    /**
     * 设备主键（闸坝才有）
     */
    private Integer stcdId;
    /**
     * 河道编号
     */
    private String river;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;
    /**
     * '处理人'
     */
    private String dispose;
    /**
     * '处理人联系方式'
     */
    private String disposePhone;
    /**
     * 发送状态0未发送1发送
     */
    private Integer state;
    /**
     * 接收状态0未接收1已接收
     */
    private Integer reception;

    /**
     * 预警处理-文件上传
     */
    private List<FileBaseEsr> file;
}
