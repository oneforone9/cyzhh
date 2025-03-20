package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/21 17:31
 */
@Data
public class RiverSectionWaterViewEsr extends Esr {

    /**
     * 案件id
     */
    private String caseId;

    /**
     * 河流id
     */
    private String riverId;

    /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 河道水位
     */
    private String riverZ;

    /**
     * 步长
     */
    private String step;

    /**
     * 左岸高程
     */
    private BigDecimal altitudeLeft;

    /**
     * 右岸高程
     */
    private BigDecimal altitudeRight;

    /**
     * 河底高程
     */
    private BigDecimal altitudeBottom;

    /**
     * 经度
     */
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    private BigDecimal lttd;
}
