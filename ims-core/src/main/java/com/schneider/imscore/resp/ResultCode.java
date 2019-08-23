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

    // 未上传图片
    ILLEGAL_PARAM("300","Please upload pictures"),
    // 图片违规
    IMAGE_BLOCK("400", "image violations"),
    // 单张图片处理失败
    SINGLE_IMAGE_CHECK_ERROR("401", "check image error,task process fail"),
    // 图片检测失败
    WHOLE_IMAGE_CHECK_ERROR("402", "the whole image scan request failed")
    ;


    private String code;
    private String desc;

}
