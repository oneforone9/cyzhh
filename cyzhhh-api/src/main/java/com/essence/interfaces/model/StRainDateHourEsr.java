package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 小时雨量返回实体
 *
 * @author tyy
 * @since 2024-07-20 11:04:30
 */

@Data
public class StRainDateHourEsr extends Esr {

    private static final long serialVersionUID = -18071906149049482L;


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
