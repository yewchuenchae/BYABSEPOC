package com.schneider.imscore.vo.log;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liyuan
 * @createDate 2019/09/16 16:44
 * @Description
 */
@Data
public class ImageSearchLogVO implements Serializable {

    private static final long serialVersionUID = 8769674930242116809L;
    /**调用总时长（ms）*/
    private Long wholeApiTime;
    /**请求数量*/
    private Long requestCount;
    /**平均调用时长*/
    private String averageApiTime;
}
