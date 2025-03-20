package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 返回实体
 *
 * @author zhy
 * @since 2023-04-22 10:55:05
 */

@Data
public class StForecastSectionEsr extends Esr {

    private static final long serialVersionUID = -31525342368239760L;


    /**
     * 主键
     */
    private String id;


    /**
     * 类型 ZZ 水位站 ZQ流量站
     */
    private String sttp;


    /**
     * 录入时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;


    /**
     * 站点名称
     */
    private String sectionName;


    /**
     * 站点数据值
     */
    private String value;


    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 方案id
     */
    private String caseId;
    /**
     * 流量1水位2
     */
    private String type;

    private BigDecimal jd;

    private BigDecimal wd;
}
