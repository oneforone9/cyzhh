package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 模型断面基础表返回实体
 *
 * @author zhy
 * @since 2023-04-19 18:15:47
 */

@Data
public class StSectionModelEsr extends Esr {

    private static final long serialVersionUID = -41640999356473760L;

    private Integer id;

    /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 河系id
     */
    private Integer riverId;

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
     * 桩号
     */
    private String pileNumber;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 经度
     */
    BigDecimal lgtd;

    /**
     * 纬度
     */
    BigDecimal lttd;
}
