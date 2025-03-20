package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esu;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 模型断面基础表更新实体
 *
 * @author zhy
 * @since 2023-04-19 18:15:45
 */

@Data
public class StSectionModelEsu extends Esu {

    private static final long serialVersionUID = 909692071301519246L;

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
