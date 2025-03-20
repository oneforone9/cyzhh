package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 设备在线达标配置
 */
@Data
@TableName("st_device_qualified")
public class DeviceQualifiedEntity {
    /**
     * 日期类型  1 年 2 月 3 日
     */
    private String dateType;
    /**
     * 设备类型 ZZ ZQ
     */
    private String deviceType;
    /**
     * 最小数值
     */
    private BigDecimal minValue;
    /**
     * 最大数值
     */
    private BigDecimal maxValue;
    /**
     * yyyy-MM
     */
    private String tm;
    /**
     * 达标基数
     */
    private Integer condNum;
}
