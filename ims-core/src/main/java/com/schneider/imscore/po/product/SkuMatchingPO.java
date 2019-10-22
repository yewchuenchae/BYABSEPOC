package com.schneider.imscore.po.product;

/**
 * @author liyuan
 * @version : SkuMatchingPO.java, v 0.1 2019/08/25 15:35 by Exp $$
 * @Description 施耐德产品和非失败的产品对应PO
 */
public class SkuMatchingPO {
    private Long id;

    private String competitorSku;

    private String schneiderElectricSku;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompetitorSku() {
        return competitorSku;
    }

    public void setCompetitorSku(String competitorSku) {
        this.competitorSku = competitorSku == null ? null : competitorSku.trim();
    }

    public String getSchneiderElectricSku() {
        return schneiderElectricSku;
    }

    public void setSchneiderElectricSku(String schneiderElectricSku) {
        this.schneiderElectricSku = schneiderElectricSku == null ? null : schneiderElectricSku.trim();
    }
}