package com.schneider.imscore.resp;

import java.io.Serializable;

/**
 * @author : yulijun
 * @date Date : 2018年10月28日 16:01
 * @Description 返回结果集
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -8147136040535828055L;

    private String code;

    private String message;

    private transient T data;

    public Result() {
    }

    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result buildSuccess() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc());
    }

    public static Result buildSuccess(Object data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc(), data);
    }

    public static Result buildFailed() {
        return new Result(ResultCode.FAILED.getCode(), ResultCode.FAILED.getDesc());
    }

    public static Result buildFailed(ResultCode resultCode) {
        return new Result(resultCode.getCode(), resultCode.getDesc());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
