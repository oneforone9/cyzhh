package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 水位流量站报警阀值配置表更新实体
 *
 * @author zhy
 * @since 2023-02-25 16:56:29
 */

@Data
public class StStbprpAlarmEsu extends Esu {

    private static final long serialVersionUID = -70246055775875744L;

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
