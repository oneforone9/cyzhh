package com.essence.interfaces.model;

import lombok.Data;

@Data
public class YuShiVideoModel {
    /**
     * 设备序列号，必填
     */
    private String deviceSerial;
    /**
     * 通道号，必填。IPC 填 0，NVR 从 1 开始
     */
    private int channelNo;
    /**
     * 过期时长，单位：秒。针对 HLS/RTMP/FLV 设置有效期，相对时间；30 秒 - 7 天。非必填，默认为 1 天
     */
    private Integer expireTime;
    /**
     * 流播放协议，必填。2 - HLS、3 - RTMP、4 - FLV
     */
    private int protocol;
    /**
     * 视频清晰度，必填。0 - 高清(主码流)，1 - 标清(辅码流)，2 - 流畅(第三流)
     */
    private int quality;
    /**
     * 协议地址的本地录像/云存储录像回放开始时间，UTC 时间戳。非必填
     */
    private Long startTime;
    /**
     * 协议地址的本地录像/云存储录像回放结束时间，UTC 时间戳。非必填
     */
    private Long endTime;
    /**
     * 协议地址的类型，非必填。1 - 直播，2 - 本地录像回放，3 - 云存储录像回放(仅 HLS)，默认为 1
     */
    private Integer type;
}