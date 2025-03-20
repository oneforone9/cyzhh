package com.essence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
@Data
@TableName("st_online_check_job")
public class OnlineCheckDeviceEntity {
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
    private String status;
    /**
     * 日期 类型
     */
    private Integer dateType;
    /**
     * 日期
     */
    private String date;
    /**
     * 1 达标 2 未能达标
     */
    private Integer checked;
}
