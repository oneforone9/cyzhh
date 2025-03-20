package com.essence.dao.entity;

import lombok.Data;

/**
 * 闸坝对应的摄像头信息
 * @Author BINX
 * @Description TODO
 * @Date 2023/6/12 14:13
 */
@Data
public class GateCameraVo {

    /**
     * 闸坝名称
     */
    private String gateName;

    /**
     * 摄像头编码
     */
    private String code;

    /**
     * 摄像头名称
     */
    private String name;

    /**
     * 摄像头类型
     */
    private String cameraType;
}
