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
 * @author zhy
 * @since 2022-10-27 15:26:31
 */

@Data
public class WorkorderProcessExEsp extends Esp {

    private static final long serialVersionUID = -6507245395567648726L;
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
     * 工单名称
     */
    @ColumnMean("order_name")
    private String orderName;

    /**
     * 当前处理人主键
     */
    @ColumnMean("person_id")
    private String personId;

    /**
     * 当前处理人
     */
    @ColumnMean("person_name")
    private String personName;

    /**
     * 工单当前状态
     */
    @ColumnMean("order_status")
    private String orderStatus;

    /**
     * 工单处理时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("order_time")
    private Date orderTime;


}
