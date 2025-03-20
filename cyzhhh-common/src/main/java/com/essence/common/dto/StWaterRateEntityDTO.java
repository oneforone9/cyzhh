package com.essence.common.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Classname StWaterRateEntityDTO
 * @Description TODO
 * @Date 2022/10/14 15:18
 * @Created by essence
 */
@Data
public class StWaterRateEntityDTO implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 站点名称
     */
    private String stationName;

    /**
     * 河底高程
     */
    private String dtmel;

    /**
     * PLCID
     */
    private String pid;

    /**
     * “0” 为模拟量  “1” 为遥信号
     */
    private String type;

    /**
     * 采集名称
     */
    private String addr;

    /**
     * 采集数据值
     */
    private String addrv;

    /**
     * 采集时间
     */
    private String ctime;

    /**
     * 月日时分秒
     */
    private String mDh;

    /**
     * SN码 需要转换一下
     */
    private String did;

    /**
     * 开始时间
     */
    @ExcelIgnore
    private String startTime;
    /**
     * 结束时间
     */
    @ExcelIgnore
    private String endTime;

    /**
     * 瞬时流量
     */
    private String momentRate;

    /**
     * 当前瞬时流量
     */
    private String preMomentRate;

    /**
     * 瞬时河道水深
     */
    private String momentRiverPosition;

    /**
     * 电压
     */
    private String voltage;

    /**
     * 水深
     */
    private BigDecimal waterDeep;

    /**
     * 流量状态 1 正常 2 流速低下
     */
    private Integer flowStatus;

    /**
     * 警戒水位
     */
    @ExcelIgnore
    private BigDecimal wrz;

    /**
     * 最高水位(改为"保证水位")
     */
    @ExcelIgnore
    private BigDecimal bhtz;
}
