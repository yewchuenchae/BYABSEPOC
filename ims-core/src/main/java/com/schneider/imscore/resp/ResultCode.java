package com.schneider.imscore.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : Hejinsheng
 * @date Date : 2018年10月28日 16:01
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("200", "SUCCESS"),
    FAILED("500", "FAILED"),
    ;


    private String code;
    private String desc;

}
