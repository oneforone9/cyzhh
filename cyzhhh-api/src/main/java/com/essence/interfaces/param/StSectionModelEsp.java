package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 模型断面基础表参数实体
 *
 * @author zhy
 * @since 2023-04-19 18:15:46
 */

@Data
public class StSectionModelEsp extends Esp {

    private static final long serialVersionUID = -88350254224754270L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 断面名称
     */
    @ColumnMean("section_name")
    private String sectionName;
    /**
     * 河系id
     */
    @ColumnMean("river_id")
    private Integer riverId;

    /**
     * 左岸高程
     */
    @ColumnMean("altitude_left")
    private BigDecimal altitudeLeft;

    /**
     * 右岸高程
     */
    @ColumnMean("altitude_right")
    private BigDecimal altitudeRight;

    /**
     * 河底高程
     */
    @ColumnMean("altitude_bottom")
    private BigDecimal altitudeBottom;

    /**
     * 桩号
     */
    @ColumnMean("pile_number")
    private String pileNumber;

    /**
     * 排序
     */
    @ColumnMean("sort")
    private Integer sort;

    /**
     * 经度
     */
    @ColumnMean("lgtd")
    BigDecimal lgtd;

    /**
     * 纬度
     */
    @ColumnMean("lttd")
    BigDecimal lttd;
}
