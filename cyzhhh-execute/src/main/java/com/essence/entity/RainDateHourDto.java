package com.essence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 雨量站 小时雨量数据
 */
@Data
@TableName("st_rain_hour_date")
public class RainDateHourDto implements Serializable {
    /**
     *id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 时间
     */
    @TableField("date")
    private Date date;
    /**
     * 场站id
     */
    @TableField("station_id")
    private String stationID;
    /**
     * 小时雨量
     */
    @TableField("rain1h")
    private String rain1h;
    /**
     * 小时雨量
     */
    @TableField("rain1h")
    private String rain3h;
    /**
     * 小时雨量
     */
    @TableField("rain1h")
    private String rain6h;
    /**
     * 小时雨量
     */
    @TableField("rain1h")
    private String rain12h;
    /**
     * 小时雨量
     */
    @TableField("rain1h")
    private String rain24h;
}
