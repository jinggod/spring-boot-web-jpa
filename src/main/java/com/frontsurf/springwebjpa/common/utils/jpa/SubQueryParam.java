package com.frontsurf.springwebjpa.common.utils.jpa;

import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/30 16:19
 * @Email xu.xiaojing@frontsurf.com
 * @Description 子查询条件
 */

public class SubQueryParam extends QueryParam{

    QueryLinkEnum queryLinkEnum;
    List<QueryParam> queryParams;

    public SubQueryParam( QueryLinkEnum queryLinkEnum, List<QueryParam> queryParams) {
        super(null);
        this.queryLinkEnum = queryLinkEnum;
        this.queryParams = queryParams;
    }

    public QueryLinkEnum getQueryLinkEnum() {
        return queryLinkEnum;
    }

    public void setQueryLinkEnum(QueryLinkEnum queryLinkEnum) {
        this.queryLinkEnum = queryLinkEnum;
    }

    public List<QueryParam> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<QueryParam> queryParams) {
        this.queryParams = queryParams;
    }
}
