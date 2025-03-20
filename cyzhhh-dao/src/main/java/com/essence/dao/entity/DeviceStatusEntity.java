package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 设备定时任务 状态统计
 */
@Data
@TableName("st_device_status")
public class DeviceStatusEntity {

    /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */

    private String stcd;
    /**
     * 离线时长
     */

    private String lineDown;
    /**
     * 1 正常  2 超警戒水位
     */

    private Integer warningStatus;
    /**
     * 在线状态 1 在线  2 不在线
     */

    private String onlineStatus;
    /**
     * 水位(实际水位/水深)
     */
    private String waterPosition;
    /**
     * 流量(实时流量)
     */
    private String waterRate;
    /**
     * 时间
     */
    private String time;
}
