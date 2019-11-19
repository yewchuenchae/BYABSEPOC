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
    /**
     * SUCCESS 编码
     */
    SUCCESS("200", "SUCCESS"),
    /**
     * FAILED 编码
     */
    FAILED("500", "FAILED"),

    // 没有产品匹配
    NO_MATCH_PRODUCTS("600","unable to find what your looking for"),
    // 没有产品匹配
    NO_MATCH_PRODUCTS_CHINESE("601","抱歉，找不到你要的产品, 请重试."),
    // 没有产品匹配
    NO_MATCH_PRODUCTS_PORTUGUESE("602","Desculpe, não consigo encontrar o que você está procurando. Por favor tente novamente."),
    // 没有产品匹配
    NO_MATCH_PRODUCTS_RUSSIAN("603","Извините, не могу найти то, что вы ищете. пожалуйста, повторите."),

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

    UNSUPPORTED_PIC_PIXELS("801","The image search works best with photo that are at least 200*200px"),

    UNABLE_FIND("802","Unable to find a match")
    ;


    private String code;
    private String desc;

}
