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
    private String brandChinese;
    private String brandPortuguese;
    private String brandRussian;
    /**种类*/
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
    /**搜索条件*/
    private String searchCriteria;
    /**语言*/
    private String language;
}
