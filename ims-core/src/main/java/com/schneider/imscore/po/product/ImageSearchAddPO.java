package com.schneider.imscore.po.product;

import lombok.Data;
import java.util.Date;

/**
 * @author liyuan
 * @version : ImageSearchAddPO.java, v 0.1 2019/08/25 15:35 by Exp $$
 * @Description 新增图搜图片PO
 */
@Data
public class ImageSearchAddPO {
    /**自增主键*/
    private Long id;
    /**请求id*/
    private String requestId;
    /**图片名称*/
    private String imageName;
    /**自定义内容*/
    private String customContent;
    /**sku*/
    private String productId;
    /**返回结果*/
    private String result;
    /**类别*/
    private String categoryId;
    /**图搜实例名称*/
    private String instanceName;
    /**创建人*/
    private String creator;
    /**修改人*/
    private String modifier;
    /**创建时间*/
    private Date created;
    /**修改时间*/
    private Date modified;
}