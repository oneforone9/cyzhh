package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cuirx
 * @Classname DeviceStatusDTO
 * @Description TODO
 * @Date 2022/10/21 13:52
 * @Created by essence
 */
@Data
public class DeviceStatusDTO implements Serializable {
    /**
     * 水位
     */
    private DeviceDataStatisticDTO waterPosition;
    /**
     * 流量
     */
    private DeviceDataStatisticDTO flowRate;

    /**
     *
     */
    private DeviceDataStatisticDTO RainRate;

}
