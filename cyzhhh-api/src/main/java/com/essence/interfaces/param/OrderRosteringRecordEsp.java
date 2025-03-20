package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 值班表生成工单记录表(避免重复生成工单)参数实体
 *
 * @author zhy
 * @since 2022-10-31 17:53:18
 */

@Data
public class OrderRosteringRecordEsp extends Esp {

    private static final long serialVersionUID = -26932357488163016L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 值班表主键
     */
    @ColumnMean("rostering_id")
    private String rosteringId;

    /**
     * 工单表主键
     */
    @ColumnMean("order_id")
    private String orderId;

    /**
     * 日期
     */
    @ColumnMean("tm")
    private String tm;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


}
