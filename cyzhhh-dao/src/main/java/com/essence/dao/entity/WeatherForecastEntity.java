package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 气象预警
 */
@Data
@Accessors(chain = true)
@TableName(value = "cy_weather_forecast")
public class WeatherForecastEntity implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 发布平台
     */
    private String publishDepartment;
    /**
     * 发布时间
     */
    private String publishTime;
    /**
     * 气象类型
     */
    private  String weatherType;
    /**
     * 气象等级
     */
    private String weatherLevel;
    /**
     * 状态
     */
    private String status;
    /**
     * 内容
     */
    private String context;
    /**
     * 防护措施
     */
    private String defence;

    private String msg;

    private String icon;
    /**
     * 现在预警类型
     */
    private String nowWaring;
}
