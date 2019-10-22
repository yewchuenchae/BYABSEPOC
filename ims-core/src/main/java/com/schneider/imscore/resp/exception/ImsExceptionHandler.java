package com.schneider.imscore.resp.exception;

import com.schneider.imscore.resp.Result;
import com.schneider.imscore.resp.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ImsExceptionHandler {

    /**
     * 全局异常捕获：Exception.class
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result apiExceptionHandler(Exception ex) {
        return Result.buildFailed(ResultCode.FAILED.getCode(), ex.getMessage());
    }

    /**
     * 全局异常捕获：BizException.class
     * @param bex
     * @return
     */
    @ExceptionHandler(BizException.class)
    public Result apiExceptionHandler(BizException bex) {
        return Result.buildFailed(bex.getCode(), bex.getMessage());
    }

}
