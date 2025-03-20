package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 小时雨量更新实体
 *
 * @author tyy
 * @since 2024-07-20 11:04:29
 */

@Data
public class StRainDateHourEsu extends Esu {

    private static final long serialVersionUID = -90805463593224098L;


    /**
     * 主键
     */
    private String id;


    /**
     * 时间
     */
    private Date date;


    /**
     * 雨量站id
     */
    private String stationId;


    /**
     * 小时雨量
     */
    private BigDecimal hourRain;


}
