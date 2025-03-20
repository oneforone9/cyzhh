package com.essence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("st_device_fell")
@Data
public class DeviceFellEntity {
    /**
     * 设备类型
     */
    private String type;
    /**
     * 日期类型
     */
    private Integer dateType;
    /**
     * 在线
     */
    private String upValue;
    /**
     * 离线
     */
    private String downValue;
    /**
     * 百分比
     */
    private String percent;

    private String date;
}
