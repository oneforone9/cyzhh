package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 实体
 *
 * @author majunjie
 * @since 2023-04-22 10:55:03
 */
@Data
public class StForecastSectionImport  {

    /**
     * 站点名称
     */
    @ExcelProperty(value="站点名称",index=0)
    private String sectionName;
    /**
     * 类型 ZZ水位站 ZQ流量站
     */
    @ExcelProperty(value="类型(ZZ水位站 ZQ流量站)",index=1)
    private String sttp;
    /**
     * 录入时间
     */
    @ExcelProperty(value="录入时间",index=2)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
    /**
     * 站点数据值
     */
    @ExcelProperty(value="站点数据值",index=3)
    private String value;
}
