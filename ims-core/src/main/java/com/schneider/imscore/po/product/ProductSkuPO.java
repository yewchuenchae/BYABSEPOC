package com.schneider.imscore.po.product;

import lombok.Data;

import java.util.Date;

@Data
public class ProductSkuPO {
    private Long id;

    private String reference;

    private String brand;

    private String brandChinese;

    private String brandPortuguese;

    private String brandRussian;

    private String family;

    private String familyChinese;

    private String familyPortuguese;

    private String familyRussian;

    private String category;

    private String categoryChinese;

    private String categoryPortuguese;

    private String categoryRussian;

    private String descriptionOcr;

    private String description;

    private String descriptionChinese;

    private String descriptionPortuguese;

    private String descriptionRussian;

    private String ossKey;

    private String creator;

    private String modifier;

    private Date created;

    private Date modified;
}