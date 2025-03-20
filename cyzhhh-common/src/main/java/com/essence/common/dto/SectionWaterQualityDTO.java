package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * SectionWaterQualityDTO
 *
 * @author bird
 * @date 2022/11/19 15:26
 * @Description
 */
@Data
public class SectionWaterQualityDTO implements Serializable {

    /**
     * 水质主键
     */
    private String id;

    /**
     * 断面主键
     */
    private Integer sectionId;

    /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 断面类型
     */
    private Integer sectionType;


    /**
     * 水质期间(年月)
     */
    private String qualityPeriod;

    /**
     * 水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;6 无水/结冰；)
     */
    private Integer qualityLevel;

    /**
     * 平均水质等级
     */
    private Integer averageLevel;

    /**
     * 经度
     */
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    private BigDecimal lttd;

    /**
     * 断面类型
     */
    private String type;

    /**
     * 水质等级
     */
    private String levelRemark;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 河流名称
     */
    private String riverName;

}
