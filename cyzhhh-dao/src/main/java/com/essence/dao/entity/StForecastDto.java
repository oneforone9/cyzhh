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
 * 预警报表实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_forecast")
public class StForecastDto extends Model<StForecastDto> {

    private static final long serialVersionUID = -70195842131365772L;
        
    /**
     * 主键
     */
    @TableId(value = "forecast_id", type = IdType.INPUT)
    private String forecastId;

    /**
     * 预警类型预警类型   水位站：1.水位超警戒17设备离线
     * 流量站 17设备离线
     * 闸坝 3阀门超载4阀门欠闸5左荷重超载6右荷重超载7PLC开度超限8PLC超载报警9电压异常10电流异常17设备离线
     * 泵站11设备故障12流量超上限13压力超上限14液位超上限15电能异常16格栅异常17设备离线
     */
    @TableField("forecast_type")
    private String forecastType;
    
    /**
     * 站点编号
     */
    @TableField("stcd")
    private String stcd;
    
    /**
     * 站点名称
     */
    @TableField("stnm")
    private String stnm;
    
    /**
     * 所属河道
     */
    @TableField("rvnm")
    private String rvnm;
    
    /**
     * 预警来源
     */
    @TableField("source_type")
    private String sourceType;
    
    /**
     * 现场负责人
     */
    @TableField("xcfzr")
    private String xcfzr;
    
    /**
     * 现场负责人联系方式
     */
    @TableField("xcfzr_phone")
    private String xcfzrPhone;
    /**
     * 现场负责人需要和用户ID
     */
    @TableField("xcfzr_user_id")
    private String xcfzrUserId;
    /**
     * 报警时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("police_time")
    private Date policeTime;
    
    /**
     * 预警状态0待处理1处理中2已处理
     */
    @TableField("forecast_state")
    private Integer forecastState;
    /**
     * 设备类型1闸坝2泵站3水位站4流量站
     */
    @TableField("device_type")
    private String deviceType;
    /**
     * 原因描述
     */
    @TableField("reason")
    private String reason;
    /**
     * 设备主键（闸坝才有）
     */
    @TableField("stcd_id")
    private Integer stcdId;
    /**
     * 河道编号
     */
    @TableField("river")
    private String river;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @TableField("lgtd")
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @TableField("lttd")
    private Double lttd;
    /**
     * '处理人'
     */
    @TableField("dispose")
    private String dispose;
    /**
     * '处理人联系方式'
     */
    @TableField("dispose_phone")
    private String disposePhone;
    /**
     * 发送状态0未发送1发送
     */
    @TableField("state")
    private Integer state;
    /**
     * 接收状态0未接收1已接收
     */
    @TableField("reception")
    private Integer reception;
}
