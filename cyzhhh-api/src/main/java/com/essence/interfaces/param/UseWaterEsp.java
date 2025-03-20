package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 用水量参数实体
 *
 * @author zhy
 * @since 2023-01-04 17:18:04
 */

@Data
public class UseWaterEsp extends Esp {

    private static final long serialVersionUID = -81499980471552624L;

                    @ColumnMean("id")
    private String id;
        /**
     * 日期
     */            @ColumnMean("date")
    private String date;
        /**
     * 用水量
     */            @ColumnMean("use_info")
    private String useInfo;
    /**
     * 更新时间
     */
    @ColumnMean("update_time")
    private String updateTime;

}
