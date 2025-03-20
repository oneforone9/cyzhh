package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class DeviceFellStatusDTO implements Serializable {
    /**
     * 日期
     */
    private String date;
    /**
     * 在线数
     */

    private Integer online;
    /**
     * 离线数量
     */
    private Integer down;
    /**
     * 总计数量
     */

    private Integer total;
}
