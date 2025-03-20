package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 雨量站 每一天的雨量累加
 */
@Data
@TableName("st_rain_day_date")
public class RainDayCountDto implements Serializable {
    /**
     *id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 时间
     */
    @TableField("date")
    private String date;
    /**
     * 场站id
     */
    @TableField("station_id")
    private String stationID;
    /**
     * 小时雨量
     */
    @TableField("day_count")
    private BigDecimal dayCount;
}
