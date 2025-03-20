package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * 设备在线达标配置
 */
@TableName("st_device_data_statistic")
@Data
public class DeviceDataStatisticEntity implements Serializable {
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
