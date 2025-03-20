package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 设备巡检摄像头巡检计划日期参数实体
 *
 * @author majunjie
 * @since 2025-01-08 17:55:49
 */

@Data
public class XjSxtJhTimeEsp extends Esp {

    private static final long serialVersionUID = -48973144747269773L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 周计划时间id
     */
    @ColumnMean("zjh_id")
    private String zjhId;
    /**
     * 计划id
     */
    @ColumnMean("jh_id")
    private String jhId;

}
