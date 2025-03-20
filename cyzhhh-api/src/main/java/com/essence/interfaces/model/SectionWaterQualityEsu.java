package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 断面水质更新实体
 *
 * @author bird
 * @date 2022/11/19 17:15
 * @Description 目前用在上传
 */
@Data
public class SectionWaterQualityEsu extends Esu {


    /**
     * 断面名称
     */
    @ExcelProperty(index = 0)
    private String sectionName;


    /**
     * 水质期间(年月)
     */
    @ExcelProperty(index = 1)
    private String qualityPeriod;


    /**
     * 水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;6 无水/结冰；)
     */
    @ExcelProperty(index = 2)
    private String qualityLevel;


    /**
     * 平均水质等级
     */
    @ExcelProperty(index = 3)
    private String averageLevel;

}
