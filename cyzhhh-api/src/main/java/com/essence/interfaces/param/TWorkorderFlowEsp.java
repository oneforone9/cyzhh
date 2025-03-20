package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单处理过程表参数实体
 *
 * @author majunjie
 * @since 2023-06-07 10:31:28
 */

@Data
public class TWorkorderFlowEsp extends Esp {

    private static final long serialVersionUID = -44562511680609667L;

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
     * 当前操作人主键
     */
    @ColumnMean("person_id")
    private String personId;
    /**
     * 当前操作人
     */
    @ColumnMean("person_name")
    private String personName;
    /**
     * 工单操作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("order_time")
    private Date orderTime;
    /**
     * 操作 1-生产工单   2-派发工单  3-开启工单  4-完成工单   5-审核工单  7-催办工单  -1 终止工单  -2 审核工单
     */
    @ColumnMean("operation")
    private String operation;


}
