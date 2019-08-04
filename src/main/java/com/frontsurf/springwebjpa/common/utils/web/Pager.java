package com.frontsurf.springwebjpa.common.utils.web;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author zou.shiyong
 * @Description 分页返回参数实体类
 * @date 2017/12/23 16:40
 * @Email zou.shiyong@frontsurf.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pager<T> {
    private List<T> list;
    private Boolean first;
    private Boolean last;
    private Long total;
    private Integer totalPages;
    private Integer pageSize;
    private Boolean previous;
    private Boolean next;
    private Integer pageNum;

    public Pager(List<T> list, Integer pageNum, Integer pageSize,Long total) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    public Pager(List<T> list, Boolean first, Boolean last, Long total, Integer totalPages, Integer pageSize, Integer pageNum) {
        this.list = list;
        this.first = first;
        this.last = last;
        this.total = total;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public Pager(List<T> list, Boolean first, Boolean last, Long total, Integer totalPages, Integer pageSize,Integer pageNum, Boolean previous, Boolean next) {
        this.list = list;
        this.first = first;
        this.last = last;
        this.total = total;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.previous = previous;
        this.previous = next;
        this.pageNum=pageNum;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getPrevious() {
        return previous;
    }

    public void setPrevious(Boolean previous) {
        this.previous = previous;
    }

    public Boolean getNext() {
        return next;
    }

    public void setNext(Boolean next) {
        this.next = next;
    }
}
