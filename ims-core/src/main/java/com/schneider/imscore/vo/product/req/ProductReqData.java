package com.schneider.imscore.vo.product.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liyuan
 * @createDate 2019/08/26 13:39
 * @Description
 */
@Data
public class ProductReqData implements Serializable {

    private static final long serialVersionUID = 9152843176736649224L;

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
}