package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 值班表生成工单记录表(避免重复生成工单)更新实体
 *
 * @author zhy
 * @since 2022-10-31 17:53:17
 */

@Data
public class OrderRosteringRecordEsu extends Esu {

    private static final long serialVersionUID = -10894748423095929L;


    /**
     * 主键
     */
    private String id;

    /**
     * 值班表主键
     */
    private String rosteringId;

    /**
     * 工单表主键
     */
    private String orderId;

    /**
     * 日期
     */
    private String tm;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


}
