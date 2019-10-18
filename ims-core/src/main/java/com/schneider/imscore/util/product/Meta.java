package com.schneider.imscore.util.product;

import lombok.Data;

import java.util.List;

/**
 * @author liyuan
 * @createDate 2019/08/19 17:19
 * @Description
 */
@Data
public class Meta {
    private String operator;

    private String item_id;

    private String cat_id;

    private List<String> pic_list;

    private String cust_content;

}
