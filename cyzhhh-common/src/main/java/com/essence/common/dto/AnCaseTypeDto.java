package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AnCaseTypeDto implements Serializable {
    /**
     * 纬度名称
     */
    private String caseType;
    /**
     * 数量
     */
    private BigDecimal num;
    /**
     * 占比
     */
    private BigDecimal percent;
}
