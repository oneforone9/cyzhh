package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 设备达标统计分析
 */
@Data
public class OnlineCheckDeviceDTO implements Serializable {
    /**
     * 总计
     */
    private Integer total;
    /**
     * 在线数量
     */
    private Integer online;
    /**
     * 在线率
     */
    private BigDecimal onlinePercent;

    /**
     * 测站id
     */
    private String stcd;

    /**
     * 用于-设备感知-设备在线-达标统计分析 1在线 2 离线
     */
    private List<Integer> status;
    /**
     * 日期 类型
     */
    private Integer dateType;
    /**
     * 日期
     */
    private String date;

}
