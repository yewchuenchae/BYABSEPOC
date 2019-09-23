package com.schneider.imscore.po.product;

import lombok.Data;

import java.util.Date;

/**
 * @author liyuan
 * @createDate 2019/09/16 16:03
 * @Description
 */
@Data
public class ImageSearchLogPO {
    private Long id;

    private String ipAddress;

    private Integer ocrTime;

    private Integer imageSearchTime;

    private Integer wholeApiTime;

    private Integer responseTime;

    private String ocrResult;

    private String creator;

    private String modifier;

    private Date created;

    private Date modified;
}
