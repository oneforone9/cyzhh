package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 水系联调-工程调度-调度预案参数实体
 *
 * @author majunjie
 * @since 2023-06-02 12:39:26
 */

@Data
public class StWaterEngineeringDispatchEsp extends Esp {

    private static final long serialVersionUID = 893027324989431676L;

    @ColumnMean("id")
    private String id;
    /**
     * 调度id
     */
    @ColumnMean("st_id")
    private String stId;
    /**
     * 汛期日常
     */
    @ColumnMean("flood_season")
    private String floodSeason;
    /**
     * 蓝色预警
     */
    @ColumnMean("b_warning")
    private String bWarning;
    /**
     * 黄色预警
     */
    @ColumnMean("y_warning")
    private String yWarning;
    /**
     * 橙色、红色预警
     */
    @ColumnMean("r_warning")
    private String rWarning;


}
