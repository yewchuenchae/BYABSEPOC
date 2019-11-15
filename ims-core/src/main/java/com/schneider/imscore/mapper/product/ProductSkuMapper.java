package com.schneider.imscore.mapper.product;

import com.schneider.imscore.po.product.ProductSkuPO;
import com.schneider.imscore.vo.product.req.ProductReqData;
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

    /**
     * 模糊查询sku
     * @param references
     * @return
     */
    List<ProductSkuPO> listProductsLikeSku(@Param("references") List<String> references);

    /**
     * 根据ocr模糊查询说明
     * @param ocr
     * @return
     */
    List<ProductSkuPO> selectProductLikeDescription(String ocr);

    /**
     * 根据类别和sku模糊查询
     * @param productReqData
     * @return
     */
    List<ProductSkuPO> selectProductLikeSkuOrCategory(ProductReqData productReqData);

    /**
     * 更新
     * @param productSkuPO
     * @return
     */
    int update(ProductSkuPO productSkuPO);

    /**
     * 更新
     * @param productSkuPO
     * @return
     */
    int updatePT(ProductSkuPO productSkuPO);

    /**
     * 查询所有产品
     * @return
     */
    List<ProductSkuPO> selectAllProducts();

    /**
     * 新增产品
     * @param productSkuPO
     * @return
     */
    int insertSelective(ProductSkuPO productSkuPO);
}
