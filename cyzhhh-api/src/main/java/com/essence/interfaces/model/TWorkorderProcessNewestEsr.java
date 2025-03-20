package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单处理过程表返回实体
 *
 * @author liwy
 * @since 2023-08-01 17:00:05
 */

@Data
public class TWorkorderProcessNewestEsr extends Esr {

    private static final long serialVersionUID = 325218598897809073L;


    /**
     * 主键
     */
    private String id;


    /**
     * 工单主键
     */
    private String orderId;


    /**
     * 当前处理人主键
     */
    private String personId;


    /**
     * 当前处理人
     */
    private String personName;


    /**
     * 工单当前状态
     */
    private String orderStatus;


    /**
     * 工单处理时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;


    /**
     * 操作
     */
    private String operation;


    /**
     * 备注
     */
    private String remark;


    /**
     * 公司负责人审批意见
     */
    private String opinion;

    /**
     * 整改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fixTime;
}
