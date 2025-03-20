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
public class WorkorderProcessEsp extends Esp {

    private static final long serialVersionUID = -12805653134433454L;


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

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;

    /**
     * 公司负责人审批意见
     */
    @ColumnMean("opinion")
    private String opinion;
    /**
     * 整改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("fix_time")
    private Date fixTime;
}
