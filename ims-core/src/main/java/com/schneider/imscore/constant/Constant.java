package com.schneider.imscore.constant;

/**
 * 常量类
 *
 * @author liyuan
 * @version $Id: Constant.java, v 0.1 2019/6/10 19:14 liyuan Exp $
 **/
public interface Constant {
    /**
     * 图搜返回结果集个数
     */
    public static final int IMAGE_SEARCH_RESULT_LIMIT = 5;

    /**
     * jpg
     */
    public static final String IMAGE_FORMAT_JPG = "jpg";

    /**
     * jpeg
     */
    public static final String IMAGE_FORMAT_JPEG = "jpeg";

    /**
     * png
     */
    public static final String IMAGE_FORMAT_PNG = "png";

    /**
     * success Code
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * lowest pixel
     */
    public static final int  LOWEST_PIXEL = 200;

    /**
     * highest pixel
     */
    public static final int  HIGHEST_PIXEL = 1024;

    /**
     * highest file size(2MB)
     */
    public static final int  HIGHEST_FILE_SIZE = 2097152;

    /**
     * 匹配度100%
     */
    public static final String SCORE = "100";

    /**
     * 施耐德
     */
    public static final String SCHNEIDER = "1";

    /**
     * 非施耐德
     */
    public static final String NON_SCHNEIDER = "0";
}
