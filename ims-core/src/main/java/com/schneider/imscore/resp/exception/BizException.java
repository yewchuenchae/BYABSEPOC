/**
 * BEYONDSOFT.COM INC
 */
package com.schneider.imscore.resp.exception;

import com.schneider.imscore.resp.ResultCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author yulijun
 * @version $Id: BizException.java, v 0.1 2019/3/13 16:43 by Exp $$
 * @Description 异常处理类
 */
@Getter
public class BizException extends RuntimeException implements Serializable {


    private static final long serialVersionUID = -2616066043454767034L;
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

}
