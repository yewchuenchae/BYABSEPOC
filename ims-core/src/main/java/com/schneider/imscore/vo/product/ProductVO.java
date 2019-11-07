package com.schneider.imscore.vo.product;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liyuan
 * @createDate 2019/08/22 10:11
 * @Description
 */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = -986211317369536226L;

    /**sku*/
    private String productId;
    /**品牌*/
    private String brand;
    private String brandChinese;
    private String brandPortuguese;
    private String brandRussian;
    /**种族*/
    private String family;
    private String familyChinese;
    private String familyPortuguese;
    private String familyRussian;
    /**类别*/
    private String category;
    private String categoryChinese;
    private String categoryPortuguese;
    private String categoryRussian;
    /**说明*/
    private String description;
    private String descriptionChinese;
    private String descriptionPortuguese;
    private String descriptionRussian;
    /**oss key*/
    private String key;
    /**url*/
    private String url;
    /**ocr说明*/
    private String descriptionOcr;
    /**分数*/
    private String score;


    @Override
    public boolean equals(Object obj) {
        ProductVO u = (ProductVO) obj;
        return this.productId.equals(u.getProductId());
    }

    @Override
    public int hashCode() {
        return this.productId.hashCode();
    }
}
