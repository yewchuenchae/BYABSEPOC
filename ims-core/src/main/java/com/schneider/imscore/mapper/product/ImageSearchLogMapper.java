package com.schneider.imscore.mapper.product;

import com.schneider.imscore.po.product.ImageSearchLogPO;
import com.schneider.imscore.vo.log.ImageSearchLogVO;

/**
 * @author liyuan
 * @createDate 2019/09/16 16:06
 * @Description
 */
public interface ImageSearchLogMapper {

    /**
     * 记录日志
     * @param imageSearchLogPO
     * @return
     */
    int saveImageSearchLog(ImageSearchLogPO imageSearchLogPO);

    /**
     * 查询时间
     * @return
     */
    ImageSearchLogVO selectImageSearchLog();
}
