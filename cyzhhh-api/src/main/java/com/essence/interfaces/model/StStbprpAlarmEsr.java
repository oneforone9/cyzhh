package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 水位流量站报警阀值配置表返回实体
 *
 * @author zhy
 * @since 2023-02-25 16:58:16
 */

@Data
public class StStbprpAlarmEsr extends Esr {

    private static final long serialVersionUID = -58829841179246493L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 报警水位(去掉)
     */
    private BigDecimal alarmLevelWater;

    /**
     * 报警流量
     */
    private BigDecimal alarmFlow;

    /**
     * 5年一遇水位
     */
    private BigDecimal once5Water;

    /**
     * 20年一遇水位
     */
    private BigDecimal once20Water;

    /**
     * 50年一遇水位
     */
    private BigDecimal once50Water;

    /**
     * 100年一遇水位
     */
    private BigDecimal once100Water;

    /**
     * 5年一遇流量
     */
    private BigDecimal once5Flow;

    /**
     * 20年一遇流量
     */
    private BigDecimal once20Flow;

    /**
     * 50年一遇流量
     */
    private BigDecimal once50Flow;

    /**
     * 100年一遇流量
     */
    private BigDecimal once100Flow;
}
