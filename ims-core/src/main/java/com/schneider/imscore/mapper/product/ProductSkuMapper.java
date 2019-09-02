package com.schneider.imscore.mapper.product;

import com.schneider.imscore.po.product.ProductSkuPO;

/**
 * @author liyuan
 * @createDate 2019/08/27 12:24
 * @Description
 */
public interface ProductSkuMapper {

    /**
     * 根据sku查询产品
     * @param reference
     * @return
     */
    ProductSkuPO selectProductByReference(String reference);
}
