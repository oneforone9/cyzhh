package com.essence.interfaces.dot;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EffectMarkStatistic implements Serializable {
    /**
     * 巡查人员名称
     */
    @ExcelProperty("巡查人员名称")
    private String userName;
    /**
     * 巡查 河段
     */
    @ExcelProperty("巡查河段")
    private String portalRiver;
    /**
     * 巡查次数
     */
    @ExcelProperty("巡查次数")
    private Integer portalNum;
    /**
     * 缺勤次数
     */
    @ExcelProperty("缺勤次数")
    private Integer absenceNum;
    /**
     * 巡查到岗率
     */
    @ExcelProperty("巡查到岗率")
    private BigDecimal portalPercent;
    /**
     * 自查案件数量
     */
    @ExcelProperty("自查案件数量")
    private Integer selfCase;
    /**
     * 单位名称
     */
    @ExcelProperty("单位名称")
    private String unitName;
    /**
     * 单位名称
     */
    @ExcelIgnore()
    private String unitId;
}
