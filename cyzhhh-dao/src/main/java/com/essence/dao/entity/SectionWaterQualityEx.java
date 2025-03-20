package com.essence.dao.entity;

import lombok.Data;

import java.math.BigDecimal;


/**
 * 断面水质情况表
 *
 * @author bird
 * @date 2022/11/19 14:37
 * @Description
 */
@Data
public class SectionWaterQualityEx extends SectionWaterQuality {

    /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 断面类型
     */
    private Integer sectionType;

    /**
     * 经度
     */
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    private BigDecimal lttd;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 河流名称
     */
    private String riverId;


}
