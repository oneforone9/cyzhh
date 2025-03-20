package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 补水口案件计算入参表更新实体
 *
 * @author BINX
 * @since 2023-05-04 19:16:25
 */
@Data
public class WaterSupplyCaseEsu extends Esu {

    private static final long serialVersionUID = -63964891839269339L;
    
    /**
     * 唯一标识
     */
    private Integer id;

    /**
     * 案件id
     */
    private String caseId;

    /**
     * 河道名称
     */
    private String riverName;

    /**
     * 补水口名称
     */
    private String waterPortName;

    /**
     * 供水能力（万m3/日）
     */
    private BigDecimal supply;

    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private BigDecimal lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private BigDecimal lttd;

    /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 序列名称
     */
    private String seriaName;

    /**
     * 补水方式 (0 - 默认固定流量; 1 - 时间序列)
     */
    private int supplyWay;

    /**
     * 时间序列流量
     */
    private String timeSupply;
}
