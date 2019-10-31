package com.schneider.imscore.util.excel;

import lombok.Data;

/**
 * @author liyuan
 * @createDate 2019/10/31 14:14
 * @Description
 */
@Data
public class ExcelProduct {
    /** 非施耐德sku */
    @ExcelVOAttribute(name = "reference", column = "C")
    private String reference;

    /** brand */
    @ExcelVOAttribute(name = "brand", column = "A")
    private String brand;

    /** family */
    @ExcelVOAttribute(name = "family", column = "X")
    private String family;

    /** category */
    @ExcelVOAttribute(name = "category", column = "Y")
    private String category;

    /** description */
    @ExcelVOAttribute(name = "description", column = "W")
    private String description;

    /** filePath */
    @ExcelVOAttribute(name = "filePath", column = "Q")
    private String filePath;


}
