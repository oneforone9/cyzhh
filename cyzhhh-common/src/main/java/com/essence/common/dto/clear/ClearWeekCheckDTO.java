package com.essence.common.dto.clear;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ClearWeekCheckDTO implements Serializable {
    /**
     * 河流名称
     */
    private String rvnm;
    /**
     * 名称
      */
    private String name;
    /**
     * 时间
     */
    private String time;
    /**
     * 氨氮
     */
    private BigDecimal nh4 ;

    private BigDecimal cod ;
    /**
     * 磷标准
     */
    private BigDecimal pStander ;
    /**
     * 氨氮 标准
     */
    private BigDecimal nh4Stander;
    /**
     * cod 标准
     */
    private BigDecimal codStander ;
    /**
     * 磷
     */
    private BigDecimal p ;
    /**
     * 透明度
     */
    private BigDecimal transmission;
    /**
     * 透明度标准
     */
    private BigDecimal transmissionStander;
//    /**
//     * 类型 1 样品 2断面
//     */
//    private Integer type;
    /**
     * 一年的第几个周
     */
    private Integer weekOfYear;

    private String year;

}
