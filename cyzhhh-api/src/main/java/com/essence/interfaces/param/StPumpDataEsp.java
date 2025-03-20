package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-04-14 11:36:09
 */

@Data
public class StPumpDataEsp extends Esp {

    private static final long serialVersionUID = -44242147123998622L;

            /**
     * 主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 设备物理地址
     */            @ColumnMean("device_addr")
    private String deviceAddr;
        /**
     * 泵站1 反馈
     */            @ColumnMean("p1_feed_back")
    private String p1FeedBack;
        /**
     * 泵站2 反馈
     */            @ColumnMean("p2_feed_back")
    private String p2FeedBack;
        /**
     * 流量
     */            @ColumnMean("flow_rate")
    private String flowRate;
        /**
     * 压力
     */            @ColumnMean("pressure")
    private String pressure;
        /**
     * 液位
     */            @ColumnMean("y_position")
    private String yPosition;
        /**
     * 泵站1 计时
     */            @ColumnMean("p1_count_time")
    private String p1CountTime;
        /**
     * 泵2 计时
     */            @ColumnMean("p2_count_time")
    private String p2CountTime;
        /**
     * A相电压
     */            @ColumnMean("a_voltage")
    private String aVoltage;
        /**
     * B相电压
     */            @ColumnMean("b_voltage")
    private String bVoltage;
        /**
     *  C相电压
     */            @ColumnMean("c_voltage")
    private String cVoltage;
        /**
     * 电流A
     */            @ColumnMean("a_electric")
    private String aElectric;
        /**
     * 电流B
     */            @ColumnMean("b_electric")
    private String bElectric;
        /**
     * 电流C
     */            @ColumnMean("c_electric")
    private String cElectric;
        /**
     * 电能
     */            @ColumnMean("electric")
    private String electric;
        /**
     * 累计流量
     */            @ColumnMean("add_flow_rate")
    private String addFlowRate;
        /**
     * 液位 高
     */            @ColumnMean("liquid_high")
    private String liquidHigh;
        /**
     * 液位 低
     */            @ColumnMean("liquid_low")
    private String liquidLow;
        /**
     * 泵1 远程
     */            @ColumnMean("p1_remote")
    private String p1Remote;
        /**
     * 泵1 运行
     */            @ColumnMean("p1_run")
    private String p1Run;
        /**
     * 泵1 故障
     */            @ColumnMean("p1_hitch")
    private String p1Hitch;
        /**
     * 泵2 远程
     */            @ColumnMean("p2_remote")
    private String p2Remote;
        /**
     * 泵2 运行
     */            @ColumnMean("p2_run")
    private String p2Run;
        /**
     * 泵2 故障
     */            @ColumnMean("p2_hitch")
    private String p2Hitch;
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("date")
    private Date date;


}
