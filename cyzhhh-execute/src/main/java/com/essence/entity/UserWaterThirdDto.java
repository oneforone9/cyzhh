package com.essence.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserWaterThirdDto implements Serializable {
     private String  wiuCd;
    /**
     * 取水 用户名称
     */
    private String  wiuNm;
     private String  zgbm;
     private String   wpcObjnm;
    /**
     * 许可取水量
     */

    private BigDecimal  ww;
    /**
     * 取水户数量
     */
    private Integer  wainNum;
    /**
     * 检测水量
     */
     private BigDecimal dayw;
    private BigDecimal  sbl;
    private BigDecimal jsl;
    private BigDecimal  ycl;

}
