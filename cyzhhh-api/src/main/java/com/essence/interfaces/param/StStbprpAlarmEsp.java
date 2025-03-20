package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 水位流量站报警阀值配置表参数实体
 *
 * @author zhy
 * @since 2023-02-25 16:58:12
 */

@Data
public class StStbprpAlarmEsp extends Esp {

    private static final long serialVersionUID = 818049281229725837L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;

    /**
     * 测站编码
     */
    @ColumnMean("stcd")
    private String stcd;

    /**
     * 报警水位(去掉)
     */
    @ColumnMean("alarm_level_water")
    private BigDecimal alarmLevelWater;

    /**
     * 报警流量
     */
    @ColumnMean("alarm_flow")
    private BigDecimal alarmFlow;

    /**
     * 5年一遇水位
     */
    @ColumnMean("once_5_water")
    private BigDecimal once5Water;

    /**
     * 20年一遇水位
     */
    @ColumnMean("once_20_water")
    private BigDecimal once20Water;

    /**
     * 50年一遇水位
     */
    @ColumnMean("once_50_water")
    private BigDecimal once50Water;

    /**
     * 100年一遇水位
     */
    @ColumnMean("once_100_water")
    private BigDecimal once100Water;

    /**
     * 5年一遇流量
     */
    @ColumnMean("once_5_flow")
    private BigDecimal once5Flow;

    /**
     * 20年一遇流量
     */
    @ColumnMean("once_20_flow")
    private BigDecimal once20Flow;

    /**
     * 50年一遇流量
     */
    @ColumnMean("once_50_flow")
    private BigDecimal once50Flow;

    /**
     * 100年一遇流量
     */
    @ColumnMean("once_100_flow")
    private BigDecimal once100Flow;
}
