package com.essence.interfaces.dot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class ForecastPerformanceDto implements Serializable {
    /**
     * 1 实测 2.预测
     */
    private  Integer type;
    /**
     * 预报
     */
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
    /**
     * 雨量数据
     */
    private String addRainNum;
}
