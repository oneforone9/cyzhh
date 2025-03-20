package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 防汛调度-方案基础表返回实体
 *
 * @author zhy
 * @since 2023-04-17 16:29:56
 */

@Data
public class StCaseBaseInfoEsr extends Esr {

    private static final long serialVersionUID = 778538634524138284L;

            
    /**
     * 主键
     */
                private String id;
    
        
    /**
     * 方案名称
     */
                private String caseName;
    
        
    /**
     * 预报开始时间
     */
            
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date forecastStartTime;
    
        
    /**
     * 预热时间（期）
     */
            
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date preHotTime;
    
        
    /**
     * 预见期
     */
            
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date preSeeTime;
    
        
    /**
     * 步长
     */
                private Integer step;
    
        
    /**
     * 雨型
     */
                private String rainType;
    
        
    /**
     * 雨型名称
     */
                private String rainTypeName;
    
        
    /**
     * 降雨总量
     */
                private String rainTotal;
    
        
    /**
     * 更新时间
     */
            
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date updateTime;

    /**
     * 雨形 id
     */
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
