package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 工单地理信息记录表-记录工单创建时的打卡点位信息参数实体
 *
 * @author liwy
 * @since 2023-05-07 12:12:29
 */

@Data
public class TWorkorderRecordPointEsp extends Esp {

    private static final long serialVersionUID = 110677744807051347L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 工单主键
     */
    @ColumnMean("order_id")
    private String orderId;
    /**
     * 打卡点位名称
     */
    @ColumnMean("point_name")
    private String pointName;
    /**
     * 点位-经度
     */
    @ColumnMean("lgtd")
    private BigDecimal lgtd;
    /**
     * 点位-纬度
     */
    @ColumnMean("lttd")
    private BigDecimal lttd;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;

    /**
     * 是否在打卡点位完成打卡 0-未完成打卡  1-已完成打卡
     */
    @ColumnMean("is_complete_clock")
    private Integer isCompleteClock;

}
