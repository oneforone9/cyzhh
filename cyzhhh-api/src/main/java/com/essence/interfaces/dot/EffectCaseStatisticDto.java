package com.essence.interfaces.dot;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class EffectCaseStatisticDto {
    /**
     * 案件渠道类型 3 自查案件
     */
    private String caseChannelType;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 案件数量
     */
    private Integer caseNumber;
    /**
     * 占比
     */
    private BigDecimal percent;
    /**
     * 同比
     */
    private BigDecimal lastPercent;
    /**
     * 自查案件总数
     */
    private Integer selfTotal;
    /**
     * 去年同比
     */
    private BigDecimal currentPercent;
}
