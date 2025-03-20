package com.essence.common.exception;

import lombok.Data;

/**
 * @ClassName BusinessException
 * @Description 这个地方不要写exception，因为Spring是只对运行时异常进行事务回滚，
 * 如果抛出的是exception是不会进行事务回滚的。
 * @Author zhichao.xing
 * @Date 2021/9/11 15:26
 * @Version 1.0
 **/
@Data
public class BusinessException extends RuntimeException {

    /**
     * @Description 具体业务异常描述
     * @Author xzc
     * @Date 15:28 2021/9/11
     * @return
     **/
    public BusinessException(String bizMsg) {
        super(bizMsg);
    }
}