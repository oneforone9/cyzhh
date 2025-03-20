package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OnlineDeviceDetailDTO implements Serializable {
    /**
     * 场站id
     */
    private String  stcd;
    /**
     * 场站类型
     */
    private String sttp;
    /**
     * 场站名称
     */
    private String stnm;
    /**
     * 实际在线数量
     */
    private Integer factNum;
    /**
     * 总计
     */
    private Integer total;
    /**
     * 百分比
     */
    private BigDecimal percent;
    /**
     * 1.达标 2未达标
     */
    private Integer checked;
    /**
     * 河流名称：测站所属河流的中文名称。
     */
    private String rvnm;
}
