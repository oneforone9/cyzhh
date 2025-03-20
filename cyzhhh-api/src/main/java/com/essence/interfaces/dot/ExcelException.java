package com.essence.interfaces.dot;

import com.essence.interfaces.model.ExcelErrorBean;
import lombok.Data;

import java.util.List;

/**
 * 认证异常
 *
 * @author zhy
 * @since 2022/5/12
 */
@Data
public class ExcelException extends RuntimeException {

    private List<ExcelErrorBean> errorData;
    /**
     * 构造器
     *
     * @param bizMsg 异常信息
     */
    public ExcelException(String bizMsg) {
        super(bizMsg);
    }

    public ExcelException(String message, List<ExcelErrorBean> errorData) {
        super(message);
        this.errorData = errorData;
    }
}
