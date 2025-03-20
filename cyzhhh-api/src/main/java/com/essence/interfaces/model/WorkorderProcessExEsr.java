package com.essence.interfaces.model;

import lombok.Data;

/**
 * 工单处理过程表返回实体
 *
 * @author zhy
 * @since 2022-10-27 15:26:31
 */

@Data
public class WorkorderProcessExEsr extends WorkorderProcessEsr {

    private static final long serialVersionUID = 2082147938168320241L;
    /**
     * 工单名称
     */
    private String orderName;
}
