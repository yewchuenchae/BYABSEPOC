package com.schneider.imscore.mapper.product;

import com.schneider.imscore.po.product.ProductSkuPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据sku集合查询产品集合
     * @param references
     * @return
     */
    List<ProductSkuPO> listProductsBySku(@Param("references") List<String> references);
}
