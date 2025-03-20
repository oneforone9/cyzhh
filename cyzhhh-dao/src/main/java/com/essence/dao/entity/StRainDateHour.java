package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 小时雨量实体
 *
 * @author tyy
 * @since 2024-07-20 11:04:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_rain_date_hour")
public class StRainDateHour extends Model<StRainDateHour> {

    private static final long serialVersionUID = -53422298344584800L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 时间
     */
    @TableField("date")
    private Date date;

    /**
     * 雨量站id
     */
    @TableField("station_id")
    private String stationId;

    /**
     * 小时雨量
     */
    @TableField("hour_rain")
    private BigDecimal hourRain;

}
