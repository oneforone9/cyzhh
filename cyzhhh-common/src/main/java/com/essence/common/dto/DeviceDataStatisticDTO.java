package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cuirx
 * @Classname DeviceDataStatisticDTO
 * @Description TODO
 * @Date 2022/10/21 13:54
 * @Created by essence
 */
@Data
public class DeviceDataStatisticDTO implements Serializable {
    /**
     * 在线
     */
    private Integer online;
    /**
     * 总数
     */
    private Integer total;
    /**
     * 类型
     */
    private String type;
}
