package com.gzu.decoration.exception;

import com.gzu.decoration.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * 
     * @param e 业务异常
     * @return 统一返回结果
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（@RequestBody + @Valid）
     * 
     * @param e 方法参数校验异常
     * @return 统一返回结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验异常: {}", message);
        return Result.error(400, message);
    }

    /**
     * 处理参数绑定异常（表单提交 + @Valid）
     * 
     * @param e 绑定异常
     * @return 统一返回结果
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数绑定异常: {}", message);
        return Result.error(400, message);
    }

    /**
     * 处理非法参数异常
     * 
     * @param e 非法参数异常
     * @return 统一返回结果
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数异常: {}", e.getMessage());
        return Result.error(400, e.getMessage());
    }

    /**
     * 处理空指针异常
     * 
     * @param e 空指针异常
     * @return 统一返回结果
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.error("系统内部错误，请联系管理员");
    }

    /**
     * 处理其他所有未捕获的异常
     * 
     * @param e 异常
     * @return 统一返回结果
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return Result.error("系统繁忙，请稍后重试");
    }
}
