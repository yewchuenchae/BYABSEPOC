package com.schneider.imscore.util.excel;

import lombok.Data;

/**
 * @author liyuan
 * @createDate 2019/10/15 16:09
 * @Description
 */
@Data
public class ExcelSkuMatching {
    /** 非施耐德sku */
    @ExcelVOAttribute(name = "competitorSKU", column = "A")
    private String competitorSKU;

    /** 施耐德sku */
    @ExcelVOAttribute(name = "schneiderElectricSKU", column = "B")
    private String schneiderElectricSKU;
}
