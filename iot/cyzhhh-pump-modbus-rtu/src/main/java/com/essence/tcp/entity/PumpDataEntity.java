package com.essence.tcp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 泵站数据
 */
@Data
@TableName("st_pump_data")
public class PumpDataEntity {

    @TableId(value = "id",type = IdType.UUID)
    private String id;
    /**
     * 设备物理地址
     */
    private String deviceAddr;
    /**
     * 泵站1 反馈
     */
    private String p1FeedBack;
    /**
     * 泵站2 反馈
     */
    private String p2FeedBack;
    /**
     * 流量
     */
    private String flowRate;
    /**
     * 压力
     */
    private String pressure;
    /**
     * 液位
     */
    private String yPosition;
    /**
     * 泵站1 计时
     */
    private String p1CountTime;
    /**
     * 泵2 计时
     */
    private String p2CountTime;
    /**
     * A相电压
     */
    private String aVoltage;
    /**
     * B相电压
     */
    private String bVoltage;
    /**
     * C相电压
     */
    private String cVoltage;
    /**
     * 电流A
     */
    private String aElectric;
    /**
     * 电流B
     */
    private String bElectric;
    /**
     * 电流C
     */
    private String cElectric;
    /**
     * 电能
     */
    private String electric;
    /**
     * 累计流量
     */
    private String addFlowRate;
    /**
     * 液位 高
     */
    private String liquidHigh;
    /**
     * 液位 低
     */
    private String liquidLow;

    /**
     * 泵1 远程
     */
    private String p1Remote;
    /**
     * 泵1 运行
     */
    private String p1Run;
    /**
     * 泵1 故障
     */
    private String p1Hitch;
    /**
     * 泵2 远程
     */
    private String p2Remote;
    /**
     * 泵2 运行
     */
    private String p2Run;
    /**
     * 泵2 故障
     */
    private String p2Hitch;
    /**
     * 泵3 远程
     */
    private String p3Remote;
    /**
     * 泵3 运行
     */
    private String p3Run;
    /**
     * 泵3 故障
     */
    private String p3Hitch;

    private Date date;

}
