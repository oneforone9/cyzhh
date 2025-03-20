package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserWaterStatisticDto implements Serializable {
    /**
     * 取水户
     */
    private String userName;
    /**
     * 取水 口
     */
    private Integer outNum;
    /**
     * 许可水量
     */
    private BigDecimal water;
    /**
     * 检测水量
     */
    private BigDecimal mnWater;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 机井类别
     */
    private String type;
    /**
     * 证号
     */
    private String eleNum;
}
