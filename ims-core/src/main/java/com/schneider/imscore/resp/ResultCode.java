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
    // 单张图片处理失败
    SINGLE_IMAGE_CHECK_ERROR("401", "check image error,task process fail"),
    // 图片检测失败
    WHOLE_IMAGE_CHECK_ERROR("402", "the whole image scan request failed"),
    // OSS上传失败
    OSS_UPLOAD_ERROR("403","OSS upload error"),
    // 图像搜索api调用失败
    IMAGE_SEARCH_ERROR("600","image search api error"),
    // ocr api调用失败
    OCR_ERROR("700","ocr api error"),
    // 新增图像搜索api调用失败
    IMAGE_SEARCH_ADD_ERROR("800","add image search  api error"),
    ;


    private String code;
    private String desc;

}
