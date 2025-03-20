package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 工单地理信息记录表-记录工单创建时的打卡点位信息返回实体
 *
 * @author liwy
 * @since 2023-05-07 12:12:37
 */

@Data
public class TWorkorderRecordPointEsr extends Esr {

    private static final long serialVersionUID = -48575594263258254L;


    /**
     * 主键
     */
    private String id;


    /**
     * 工单主键
     */
    private String orderId;


    /**
     * 打卡点位名称
     */
    private String pointName;


    /**
     * 点位-经度
     */
    private BigDecimal lgtd;


    /**
     * 点位-纬度
     */
    private BigDecimal lttd;


    /**
     * 备注
     */
    private String remark;

    /**
     * 是否在打卡点位完成打卡 0-未完成打卡  1-已完成打卡
     */
    private Integer isCompleteClock;


}
