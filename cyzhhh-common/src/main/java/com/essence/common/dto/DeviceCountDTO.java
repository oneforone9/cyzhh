package com.essence.common.dto;

import lombok.Data;
import org.apache.catalina.util.Introspection;

import java.beans.IntrospectionException;
import java.io.Serializable;

/**
 * 设备感知 数量统计
 */
@Data
public class DeviceCountDTO implements Serializable {
    /**
     * 水位站 数量
     */
    private Integer zzCount;
    /**
     * 流量站 数量
     */
    private Integer zqCount;
    /**
     * 功能监控 数量
     */
    private Integer functionMonitor;
    /**
     * 安防监控 数量
     */
    private Integer protectMonitor;

    /**
     * 井房监控 数量
     */
    private Integer jfMonitor;

    /**
     * 泵站 数量
     */
    private Integer dpCount;
    /**
     * 水闸 数量
     */
    private Integer ddCount;
    /**
     * 普通摄像头
     */
    private Integer commonCameraCount;
    /**
     * 丽旧摄像头
     */
    private Integer ljCameraCount;
    /**
     * 边闸数量
     */
    private Integer sideGateCount;

    /**
     * 水坝数量
     */
    private Integer sbCount;
    /**
     * 所有摄像头总数
     */
    private Integer commonCameraAll;

    /**
     * 雨量站数量
     */
    private Integer ppCount;

}
