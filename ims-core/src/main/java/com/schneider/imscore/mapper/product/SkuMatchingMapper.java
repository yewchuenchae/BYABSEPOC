package com.schneider.imscore.mapper.product;

import com.schneider.imscore.po.product.SkuMatchingPO;

import java.util.List;

/**
 * @author liyuan
 * @createDate 2019/10/15 15:55
 * @Description
 */
public interface SkuMatchingMapper {

    /**
     * 新增施耐德和非施耐德sku关系
     * @param skuMatchingPO
     * @return
     */
    int saveSkuMatching(SkuMatchingPO skuMatchingPO);

    /**
     * 根据非施耐德sku查询关联关系
     * @param competitorSku
     * @return
     */
    List<SkuMatchingPO> selectMatchByCompetitorSku(String competitorSku);
}
