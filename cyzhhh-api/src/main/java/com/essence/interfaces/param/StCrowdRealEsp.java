package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 清水的河 - 实时游客表参数实体
 *
 * @author zhy
 * @since 2023-02-28 11:44:52
 */

@Data
public class StCrowdRealEsp extends Esp {

    private static final long serialVersionUID = 565130263786265308L;

    /**
     * 主见
     */
    @ColumnMean("id")
    private String id;
    /**
     * 热点区域
     */
    @ColumnMean("area")
    private String area;
    /**
     * 河流名称
     */
    @ColumnMean("rvnm")
    private String rvnm;
    /**
     * 水管单位
     */
    @ColumnMean("water_unit")
    private String waterUnit;
    /**
     * 人数
     */
    @ColumnMean("num")
    private Integer num;
    /**
     * 时间 :yyyy-MM-dd HH:mm
     */
    @ColumnMean("date")
    private String date;


}
