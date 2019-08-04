package com.frontsurf.springwebjpa.common.utils.web;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/11 10:40
 * @Email xu.xiaojing@frontsurf.com
 * @Description 列表请求参数类
 */

public class ListRequestParam {

    /**
     * 页码，默认是第一页
     */
    Integer pageNum = 1;

    /**
     * 页大小，默认是第15页
     */
    Integer pageSize = 15;

    /**
     * 排序字段的下标位
     */
    Byte orderBy = null;

    /**
     * 排序方式，是否是降序，1-降序，0-升序
     */
    Byte desc = null;

    public Integer getPageNum() {
        // JPA分页从开始
        return pageNum - 1;
    }

    public void setPageNum(Integer pageNum) {

        this.pageNum = pageNum == null ? 1 : pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize == null ? 15 : pageSize;
    }

    public Byte getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Byte orderBy) {
        this.orderBy = orderBy;
    }

    public Byte getDesc() {
        return desc;
    }

    public void setDesc(Byte desc) {
        this.desc = desc;
    }

    public Integer getHumanPageNum(){
        return pageNum;
    }
}
