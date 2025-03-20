package com.essence.interfaces.dot;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 超水位闸坝实体
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/10 14:00
 */
@Data
public class WaterOverLevelDto {

    /**
     * 闸坝名称
     */
    private String stnm;

    /**
     * 所属河道
     */
    private String riverName;

    /**
     * 实时水位
     */
    private BigDecimal actualTimeLevel;

    /**
     * 高水位 (7月20日~8月15日)
     */
    private String highWaterLevel;

    /**
     * 中水位 (8月16日~第二年7月19日)
     */
    private String middleWaterLevel;

    /**
     * 是否超出水位 (已超) (只展示已超)
     */
    private String isOverLevel;
}
