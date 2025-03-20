package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-03-16 16:41:57
 */

@Data
public class CyWeatherForecastEsp extends Esp {

    private static final long serialVersionUID = 130918896852974465L;

                    @ColumnMean("id")
    private String id;
        /**
     * 气象发布部门
     */            @ColumnMean("publish_department")
    private String publishDepartment;
        /**
     * 发布时间
     */            @ColumnMean("publish_time")
    private String publishTime;
        /**
     * 气象类型
     */            @ColumnMean("weather_type")
    private String weatherType;
        /**
     * 气象等级
     */            @ColumnMean("weather_level")
    private String weatherLevel;
        /**
     * 状态
     */            @ColumnMean("status")
    private String status;
        /**
     * 内容
     */            @ColumnMean("context")
    private String context;
        /**
     * 防护措施
     */            @ColumnMean("defence")
    private String defence;
        /**
     * 消息
     */            @ColumnMean("msg")
    private String msg;
        /**
     * 图标
     */            @ColumnMean("icon")
    private String icon;
        /**
     * 预警类型
     */            @ColumnMean("now_waring")
    private String nowWaring;


}
