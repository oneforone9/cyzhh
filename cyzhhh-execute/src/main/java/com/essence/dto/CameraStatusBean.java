package com.essence.dto;

import lombok.Data;

/**
 * @author zhy
 * @since 2022/10/21 16:24
 */
@Data
public class CameraStatusBean {
    /**
     * 摄像头编码
     */
    private String indexCode;
    /**
     * 在线状态
     */
    private String online;
}
