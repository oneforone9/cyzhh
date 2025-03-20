package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 接诉即办理参数实体
 *
 * @author zhy
 * @since 2023-01-04 10:39:21
 */

@Data
public class TRewardDealEsp extends Esp {

    private static final long serialVersionUID = -75932247108385576L;

            /**
     * 主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 来电时间
     */            @ColumnMean("call_time")
    private String callTime;
        /**
     * 大类
     */            @ColumnMean("big_kind")
    private String bigKind;
        /**
     * 来源渠道
     */            @ColumnMean("source")
    private String source;
        /**
     * 小类
     */            @ColumnMean("small_kind")
    private String smallKind;
        /**
     * 细类
     */            @ColumnMean("detail_kind")
    private String detailKind;


}
