package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分钟雨量
 */
@Data
@TableName("st_rain_date")
public class RainDateDto implements Serializable {
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
     * 分钟雨量
     */
    @TableField("hh_rain")
    private String hhRain;
//    /**
//     * 原始数据
//     */
//    @TableField("org_rain")
//    private String OrgRain;
//    /**
//     * 修正状态 0 未能修正 1修正
//     */
//    @TableField("status")
//    private String status;
}
