package com.essence.interfaces.model;

import lombok.Data;

/**
 * 工单处理过程表更新实体
 *
 * @author zhy
 * @since 2022-10-27 15:26:31
 */

@Data
public class WorkorderProcessExEsu extends WorkorderProcessEsu {

    private static final long serialVersionUID = 7717050955871836465L;
    /**
     * 工单名称
     */
    private String orderName;
}
