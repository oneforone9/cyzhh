package com.essence.common.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname StWaterRateEntityDTO
 * @Description TODO
 * @Date 2022/10/14 15:18
 * @Created by essence
 */
@Data
public class StWaterRateExcelDTO implements Serializable {
    /**
     * 站点名称
     */
    @ExcelProperty("站点名称")
    private String stationName;
    /**
     * 水深
     */
    @ExcelProperty("水深")
    private String waterDeep;
    /**
     * 水位(实际水位/水深)
     */
    @ExcelProperty("水位")
    private String waterPosition;
    /**
     * 高程
     */
    @ExcelProperty("高程")
    private String dtml;
    /**
     * 瞬时流量
     */
    @ExcelProperty("瞬时流量")
    private String waterRate;
    /**
     * 日期
     */
    @ExcelProperty("日期")
    private Date Date;


}
