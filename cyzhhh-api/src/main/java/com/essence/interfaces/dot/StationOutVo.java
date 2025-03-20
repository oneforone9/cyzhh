package com.essence.interfaces.dot;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/11 16:24
 */
@Data
public class StationOutVo {

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称
     */
    private String stnm;

    /**
     * 测站类型
     */
    private String sttp;

    /**
     * 监测点描述
     */
    private String monitor;

    /**
     * 时间:水位
     */
    private Map<String, String> waterLevel;

    /**
     * 时间:流量
     */
    private Map<String, String> flow;

}
