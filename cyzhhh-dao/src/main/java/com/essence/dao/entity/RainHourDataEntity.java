package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("st_rain_hour")
public class RainHourDataEntity implements Serializable {
    /**
     *id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 雨量站id
     */
    private String stationId;
    /**
     * 1 小时雨量
     */
    private String hourOne;
    /**
     * 3小时雨量
     */
    private String hourThree;
    /**
     * 6小时雨量
     */
    private String hourSix;
    /**
     * 12 小时雨量
     */
    private String hourTwelve;
    /**
     * 24 小时雨量
     */
    private String hourTwenty;
    /**
     * 时间 yyyy-MM-dd
     */
    private String date;
}
