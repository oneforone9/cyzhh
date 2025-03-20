package com.essence.interfaces.annotation;

import java.lang.annotation.*;

/**
 * 返回值与表字段对应注解
 * @author zhy
 * @since 2022/5/12 11:23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnMean {
    /**
     * 表字段值
     */
    String value() ;
}
