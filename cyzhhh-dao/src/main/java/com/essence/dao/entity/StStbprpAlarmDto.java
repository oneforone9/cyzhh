package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 水位流量站报警阀值配置表实体
 *
 * @author BINX
 * @since 2023-02-25 16:56:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_stbprp_alarm")
public class StStbprpAlarmDto extends Model<StStbprpAlarmDto> {

    private static final long serialVersionUID = 120795715633536880L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 测站编码
     */
    @TableField("stcd")
    private String stcd;

    /**
     * 报警水位(去掉)
     */
    @TableField("alarm_level_water")
    private BigDecimal alarmLevelWater;

    /**
     * 报警流量
     */
    @TableField("alarm_flow")
    private BigDecimal alarmFlow;

    /**
     * 5年一遇水位
     */
    @TableField("once_5_water")
    private BigDecimal once5Water;

    /**
     * 20年一遇水位
     */
    @TableField("once_20_water")
    private BigDecimal once20Water;

    /**
     * 50年一遇水位
     */
    @TableField("once_50_water")
    private BigDecimal once50Water;

    /**
     * 100年一遇水位
     */
    @TableField("once_100_water")
    private BigDecimal once100Water;

    /**
     * 5年一遇流量
     */
    @TableField("once_5_flow")
    private BigDecimal once5Flow;

    /**
     * 20年一遇流量
     */
    @TableField("once_20_flow")
    private BigDecimal once20Flow;

    /**
     * 50年一遇流量
     */
    @TableField("once_50_flow")
    private BigDecimal once50Flow;

    /**
     * 100年一遇流量
     */
    @TableField("once_100_flow")
    private BigDecimal once100Flow;
}
