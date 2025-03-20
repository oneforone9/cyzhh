package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备周计划参数实体
 *
 * @author majunjie
 * @since 2025-01-08 15:52:41
 */

@Data
public class XjZjhEsp extends Esp {

    private static final long serialVersionUID = -81888122784183092L;
    
    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 描述(第x周(mm-dd至mm-dd))
     */
    @ColumnMean("ms")
    private String ms;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("start_time")
    private Date startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("end_time")
    private Date endTime;
    /**
     * 年份
     */
    @ColumnMean("year")
    private Integer year;
    /**
     * 日期yyyy-mm-dd
     */
    @ColumnMean("time")
    private String time;


}
