package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设计雨型参数实体
 *
 * @author majunjie
 * @since 2023-04-24 09:57:44
 */

@Data
public class StDesigRainPatternEsp extends Esp {

    private static final long serialVersionUID = -66934411451179763L;

            /**
     * 雨型ID
     */            @ColumnMean("design_rain_pattern_id")
    private String designRainPatternId;
        /**
     * 雨型名称
     */            @ColumnMean("design_rain_pattern_name")
    private String designRainPatternName;
        /**
     * 小时数
     */            @ColumnMean("hour_count")
    private Integer hourCount;
        /**
     * 时间间隔（分钟）
     */            @ColumnMean("time_interval")
    private Integer timeInterval;
        /**
     * 雨型参数（参数之间用英文逗号隔开）
     */            @ColumnMean("param")
    private String param;
        /**
     * 创建时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("gmt_create")
    private Date gmtCreate;
        /**
     * 更新时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("gmt_modified")
    private Date gmtModified;
        /**
     * 备注
     */            @ColumnMean("remark")
    private String remark;


}
