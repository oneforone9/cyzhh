package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单处理过程表更新实体
 *
 * @author liwy
 * @since 2023-08-01 16:59:51
 */

@Data
public class TWorkorderProcessNewestEsu extends Esu {

    private static final long serialVersionUID = -19028499911486548L;

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
