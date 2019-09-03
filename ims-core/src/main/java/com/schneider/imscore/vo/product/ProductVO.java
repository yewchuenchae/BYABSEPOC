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
    /**种族*/
    private String family;
    /**类别*/
    private String category;
    /**说明*/
    private String description;
    /**oss key*/
    private String key;
    /**url*/
    private String url;

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
