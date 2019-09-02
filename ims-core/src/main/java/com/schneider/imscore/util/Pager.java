/**
 * BEYONDSOFT.COM INC
 */
package com.schneider.imscore.util;


import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页类
 *
 * @author yulijun
 * @version $Id: Pager.java, v 0.1 2017/7/13 12:19 yulijun Exp $$
 */
public class Pager<T> implements Serializable {
    private static final long serialVersionUID = -6132320072158319161L;
    /**
     * 总条数
     */
    private Long totalCount;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     *
     */
    private Integer pageSize;
    /**
     * 分页数据
     */
    private transient List<T> data;


    public static Pager create(List date, Long totalCount, Integer currentPage, Integer pageSize) {
        Pager pager = new Pager<>();
        pager.setData(date);
        pager.setPageSize(pageSize);
        pager.setTotalCount(totalCount);
        pager.setCurrentPage(currentPage);
        if (currentPage != null && pageSize != null && currentPage > 0 && pageSize > 0 && totalCount > 0) {
            pager.setTotalPage(totalCount % pageSize ==0?totalCount / pageSize:totalCount / pageSize+ 1);
        }
        return pager;
    }

    private static <T> Pager<T> getInstance(PageInfo pageInfo){
        Pager pager = new Pager<>();
        pager.setTotalCount(pageInfo.getTotal());
        pager.setCurrentPage(pageInfo.getPageNum());
        pager.setPageSize(pageInfo.getPageSize());
        return pager;
    }

    public static <T> Pager<T> newPager(PageInfo pageInfo) {
        Pager<T> pager = getInstance(pageInfo);
        pager.setData(pageInfo.getList());
        return pager;
    }





    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }
}
