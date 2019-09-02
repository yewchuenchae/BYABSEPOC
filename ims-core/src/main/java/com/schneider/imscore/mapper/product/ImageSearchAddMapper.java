package com.schneider.imscore.mapper.product;

import com.schneider.imscore.po.product.ImageSearchAddPO;

/**
 * @author liyuan
 * @createDate 2019/08/27 17:28
 * @Description
 */
public interface ImageSearchAddMapper {
    /**
     * 新增图搜图片
     * @param imageSearchAddPO
     * @return
     */
    int saveImageSearch(ImageSearchAddPO imageSearchAddPO);
}
