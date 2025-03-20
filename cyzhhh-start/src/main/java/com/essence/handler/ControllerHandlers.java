package com.essence.handler;

import com.essence.common.exception.BusinessException;
import com.essence.common.utils.ResponseResult;
import com.essence.service.exception.PaginatorException;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @ClassName ControllerHandlers
 * @Description 控制器通知  如果只想对一部分控制器通知，比如某个包下边的控制器
 * @Author zhichao.xing
 * @Date 2021/9/11 15:07
 * @Version 1.0
 **/
@Log4j2
@RestControllerAdvice(basePackages = "com.essence")
public class ControllerHandlers {
    @ExceptionHandler
    public ResponseResult<String> errorHandler(Exception e) {
        log.error(e);
        log.info("exception ", e);
        return ResponseResult.error("服务器内部错误!!!");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseResult<String> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResponseResult.error("不支持[" + e.getMethod() + "]请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<String> notFount(RuntimeException e) {
        log.error("运行时异常:", e);
        return ResponseResult.error("运行时异常:" + e.getMessage());
    }


    /**
     * 校验异常
     * //处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult<String> exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getField() + ":" + fieldError.getDefaultMessage() + "!";
        }
        return ResponseResult.error(errorMesssage);
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseResult<String> validationExceptionHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + "!";
        }
        return ResponseResult.error(errorMesssage);
    }

    /**
     * 校验异常
     * //处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseResult<String> ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<String> msgList = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msgList.add(cvl.getMessageTemplate());
        }
        return ResponseResult.error(String.join(",", msgList));
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<String> businessException(BusinessException e) {
        log.error("业务异常信息[{}]", e.getMessage(), e);
        return ResponseResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(PaginatorException.class)
    public ResponseResult<String> paginatorException(PaginatorException e) {
        log.error("查询参数粗我【{}】", e.getMessage(), e);
        return ResponseResult.error(e.getMessage());
    }

    /**
     * 违反主键或唯一约束
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseResult duplicateKeyException(HttpServletResponse response, DuplicateKeyException e) {
        log.error("违反了约束（外键、主键或唯一键）:{}", e.getMessage(), e);
        try {
            String msg = e.getRootCause().getLocalizedMessage();
            String sub = msg.substring(msg.lastIndexOf("entry") + 6, msg.lastIndexOf("for"));
            return ResponseResult.error(sub + "已存在，不可以重复添加");
        } catch (Exception ex) {
            return ResponseResult.error("内容已存在，不可以重复添加");

        }


    }

    /**
     * 批量添加违反主键或唯一约束
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseResult PersistenceException(HttpServletResponse response, PersistenceException e) {
        log.error("违反了约束（外键、主键或唯一键）:{}", e.getMessage(), e);
        try {
            String msg = e.getCause().getLocalizedMessage();
            String sub = msg.substring(msg.lastIndexOf("entry") + 6, msg.lastIndexOf("for"));
            return ResponseResult.error(sub + "已存在，不可以重复添加");
        } catch (Exception ex) {
            return ResponseResult.error("内容已存在，不可以重复添加");

        }
    }
}
