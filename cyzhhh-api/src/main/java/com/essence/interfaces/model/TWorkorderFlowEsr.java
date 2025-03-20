package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单处理过程表返回实体
 *
 * @author majunjie
 * @since 2023-06-07 10:31:31
 */

@Data
public class TWorkorderFlowEsr extends Esr {

    private static final long serialVersionUID = -88305274862219918L;


    /**
     * 主键
     */
    private String id;


    /**
     * 工单主键
     */
    private String orderId;


    /**
     * 当前操作人主键
     */
    private String personId;


    /**
     * 当前操作人
     */
    private String personName;


    /**
     * 工单操作时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;


    /**
     * 操作 1-生产工单   2-派发工单  3-开启工单  4-完成工单   5-审核工单  7-催办工单  -1 终止工单  -2 审核工单
     */
    private String operation;
}
