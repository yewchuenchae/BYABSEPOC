/**
 * BEYONDSOFT.COM INC
 */
package com.schneider.imscore.resp.exception;

import com.schneider.imscore.resp.ResultCode;

import java.io.Serializable;

/**
 * @author yulijun
 * @version $Id: BizException.java, v 0.1 2019/3/13 16:43 by Exp $$
 * @Description 异常处理类
 */
public class BizException extends Exception implements Serializable {

    private static final long serialVersionUID = -1702241321770576449L;
    private String code;
    private String message;

    public BizException(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public BizException(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getDesc();
    }
    public String getCode() {
        return this.code;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
