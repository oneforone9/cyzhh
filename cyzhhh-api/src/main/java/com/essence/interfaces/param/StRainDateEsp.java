package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-02-20 14:33:11
 */

@Data
public class StRainDateEsp extends Esp {

    private static final long serialVersionUID = 649437402190033623L;

            /**
     * 主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("date")
    private Date date;
        /**
     * 雨量站id
     */            @ColumnMean("station_id")
    private String stationId;
        /**
     * 小时雨量
     */            @ColumnMean("hh_rain")
    private String hhRain;


}
