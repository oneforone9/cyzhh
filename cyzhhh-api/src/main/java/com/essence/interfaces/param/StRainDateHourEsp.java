package com.essence.interfaces.param;


import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 小时雨量参数实体
 *
 * @author tyy
 * @since 2024-07-20 11:07:47
 */

@Data
public class StRainDateHourEsp extends Esp {

    private static final long serialVersionUID = 836060752216796314L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 时间
     */
    @ColumnMean("date")
    private Date date;
    /**
     * 雨量站id
     */
    @ColumnMean("station_id")
    private String stationId;
    /**
     * 小时雨量
     */
    @ColumnMean("hour_rain")
    private BigDecimal hourRain;

}
