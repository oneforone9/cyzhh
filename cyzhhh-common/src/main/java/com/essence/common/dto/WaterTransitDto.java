package com.essence.common.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 过境水量返回实体
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/24 11:10
 */
@Data
public class WaterTransitDto {

    /**
     * 入境水量
     */
    BigDecimal flowIn;

    /**
     * 出境水量
     */
    BigDecimal flowOut;

    /**
     * 过境水量
     */
    BigDecimal flowTransit;
}
