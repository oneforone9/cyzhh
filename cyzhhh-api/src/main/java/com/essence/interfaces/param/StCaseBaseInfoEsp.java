package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 防汛调度-方案基础表参数实体
 *
 * @author zhy
 * @since 2023-04-17 16:29:55
 */

@Data
public class StCaseBaseInfoEsp extends Esp {

    private static final long serialVersionUID = -28895821124926172L;

            /**
     * 主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 方案名称
     */            @ColumnMean("case_name")
    private String caseName;
        /**
     * 预报开始时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("forecast_start_time")
    private Date forecastStartTime;
        /**
     * 预热时间（期）
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("pre_hot_time")
    private Date preHotTime;
        /**
     * 预见期
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("pre_see_time")
    private Date preSeeTime;
        /**
     * 步长
     */            @ColumnMean("step")
    private Integer step;
        /**
     * 雨型
     */            @ColumnMean("rain_type")
    private String rainType;
        /**
     * 雨型名称
     */            @ColumnMean("rain_type_name")
    private String rainTypeName;
        /**
     * 降雨总量
     */            @ColumnMean("rain_total")
    private String rainTotal;
        /**
     * 更新时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("update_time")
    private Date updateTime;
    /**
     * 方案状态 1 未开始 2 进行中 3 计算失败 4 计算完成
     */
    @ColumnMean("status")
    private String status;
    /**
     * 雨形 id
     */
    @ColumnMean("rain_id")
    private String rainId;

    /**
     * 模型类型  1 防汛调度  2 水资源调度
     */
    @ColumnMean("model_type")
    private Integer modelType;

    /**
     * 预报方式 1 气象预测 2 设计雨型
     */
    @ColumnMean("forecast_type")
    private String forecastType;
}
