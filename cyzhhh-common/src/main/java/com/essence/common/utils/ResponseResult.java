package com.essence.common.utils;

import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author zhy
 * @date 2021/9/29
 * @Description 统一返回实体类
 */
@Log4j2
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = -7061247046738097546L;
    public final static String OK = "ok";
    public final static String ERROR = "error";
    /**
     * 状态码 成功ok 失败error
     *
     * @mock ok
     */
    private String code;
    /**
     * 信息描述
     *
     * @mock 查询成功
     */
    private String info;
    /**
     * 主体数据
     */
    private T result;

    public ResponseResult() {
    }


    public ResponseResult(String code, String info, T result) {
        this.code = code;
        this.info = info;
        this.result = result;
    }

    public static <T> ResponseResult<T> success(String info, T result) {
        return new ResponseResult(ResponseResult.OK, info, result);
    }

    public static <T> ResponseResult<T> error(String code, String info) {
        return new ResponseResult(code, info, null);
    }

    public static <T> ResponseResult<T> error(String code, String info, Object result) {
        return new ResponseResult(code, info, result);
    }

    public static <T> ResponseResult<T> error(String info) {
        return new ResponseResult(ResponseResult.ERROR, info, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    /**
     * 执行操作，并根据操作是否成功返回给客户端相应信息
     * 封装了在服务端接口中很常见的执行操作，成功返回成功标志、失败返回失败标志的通用操作，用于简化编码
     */
    public static ResponseResult op(Runnable executor) {
        return op(executor, e -> log.error(e.getMessage(), e));
    }

    /**
     * 执行操作（带自定义的失败处理），并根据操作是否成功返回给客户端相应信息
     * 封装了在服务端接口中很常见的执行操作，成功返回成功标志、失败返回失败标志的通用操作，用于简化编码
     */
    public static ResponseResult op(Runnable executor, Consumer<Exception> exceptionConsumer) {
        try {
            executor.run();
            return ResponseResult.success("操作成功", null);
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            return ResponseResult.error(e.getMessage());
        }
    }
}
