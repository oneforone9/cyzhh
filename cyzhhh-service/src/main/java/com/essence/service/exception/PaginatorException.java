package com.essence.service.exception;

import lombok.Data;

/**
 * 基础查询时 查询条件异常
 * @author zhy
 * @since 2022/5/12
 */
@Data
public class PaginatorException extends RuntimeException {
    /**
     * 构造器
     * @param bizMsg 异常信息
     */
    public PaginatorException(String bizMsg) {
        super(bizMsg);
    }
}
