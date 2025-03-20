package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 地下水埋深参数实体
 *
 * @author zhy
 * @since 2023-01-04 14:46:14
 */

@Data
public class TDeepWaterEsp extends Esp {

    private static final long serialVersionUID = -21222720566515736L;

            /**
     * 主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 年
     */            @ColumnMean("year")
    private String year;
        /**
     * 埋 信息
     */            @ColumnMean("deep_info")
    private String deepInfo;

    /**
     * 深度
     */
    @ColumnMean("deep")
    private String deep;

    /**
     * 更新时间
     */
    @ColumnMean("update_time")
    private String updateTime;
    /**
     * 1 年度 2 月度
     */
    @ColumnMean("type")
    private String type;
}
