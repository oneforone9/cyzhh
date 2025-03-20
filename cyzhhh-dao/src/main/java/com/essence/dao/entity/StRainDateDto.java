package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分钟雨量
 *
 * @author BINX
 * @since 2023-02-20 14:33:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_rain_date")
public class StRainDateDto extends Model<StRainDateDto> {

    private static final long serialVersionUID = 738651204268981714L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("date")
    private Date date;

    /**
     * 雨量站id
     */
    @TableField("station_id")
    private String stationId;

    /**
     * 分钟雨量
     */
    @TableField("hh_rain")
    private String hhRain;
    /**
     * 原始数据
     */
    @TableField("org_rain")
    private String orgRain;
    /**
     * 修正状态 0 未能修正 1修正
     */
    @TableField("status")
    private String status;


}
