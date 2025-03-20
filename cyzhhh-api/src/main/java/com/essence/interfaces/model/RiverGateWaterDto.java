package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 河流下的 水闸断面实时数据
 */
@Data
public class RiverGateWaterDto implements Serializable {
    /**
     * 水闸
     */
    private String gateName;
    /**
     * 实时水位数据
     */
    private List<String> value;
    /**
     * 时间
     */
    private List<String> time;
    /**
     * 警戒水位
     */
    private BigDecimal wrz;
    /**
     * 最高水位
     */
    private BigDecimal bhtz;
    /**
     * 高程
     */
    private  String dtmel;
}
