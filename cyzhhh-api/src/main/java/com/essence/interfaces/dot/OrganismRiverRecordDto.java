package com.essence.interfaces.dot;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrganismRiverRecordDto implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 河流 名称
     */
    private String rvnm;
    /**
     * 河流长度
     */
    private BigDecimal rvLong;
    /**
     * 河流面积
     */
    private BigDecimal rvWide;
    /**
     * 年份
     */
    private String year;
    /**
     * 河流数据
     */
    private String geom;
    /**
     * 流经区域
     */
    private String adName;

    private String unitName;
}
