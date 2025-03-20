package com.essence.interfaces.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoInfoBaseDto implements Serializable {


    /**
     * 主键 摄像头id
     */
    private Integer id;
    /**
     * 视频编码
     */
    private String code;


    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;


    /**
     * 经度
     */
    private Double lgtd;

    /**
     * 纬度
     */
    private Double lttd;

    /**
     * 视频来源（HIV 海康 HUAWEI 华为）
     */
    private String source;

    /**
     * 视频状态(0 离线 1 在线)
     */
    private String status;


    /**
     * 备注
     */
    private String remark;


    /**
     * 河系名称
     */
    private String river_name;

    /**
     * 摄像机类型
     */
    private String cameraType;

    /**
     * AI算法
     */
    private String aiAlgorithm;

    /**
     * 观鸟区
     */
    private String birdArea;

}
