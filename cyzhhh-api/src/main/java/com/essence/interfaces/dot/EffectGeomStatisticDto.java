package com.essence.interfaces.dot;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 重点和巡查位置统计
 */
@Data
public class EffectGeomStatisticDto implements Serializable {
    /**
     * 分管河道
     */
    private String rea;

    private String reaId;
    /**
     * 重点巡查位置
     */
    private String geomPosition;
    /**
     * 应巡河次数
     */
    private Integer portalNum;
    /**
     * 实际巡河次数
     */
    private BigDecimal factReaNum;
    /**
     * 缺勤次数
     */
    private Integer absenceNum;
    /**
     * 自查案件数量
     */
    private Integer selfCase;
    /**
     * 巡河员
     */
    private String userName;
    /**
     * 单位id
     */
    private String unitId;
    /**
     * 单位名称
     */
    private String unitName;

}
