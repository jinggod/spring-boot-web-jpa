package com.frontsurf.springwebjpa.common.utils.jpa;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/25 15:24
 * @Email xu.xiaojing@frontsurf.com
 * @Description 普通字段的查询参数
 */

public class FieldQueryParam extends QueryParam {


    private QueryComparison queryComparison;
    private Object value;


    /**
     * 单表查询
     *
     * @param fieldName
     * @param queryComparison
     * @param value
     */
    public FieldQueryParam(String fieldName, QueryComparison queryComparison, Object value) {
        super(fieldName);
        this.queryComparison = queryComparison;
        this.value = value;
    }

    /**
     * 单表查询
     *
     * @param fieldName
     * @param value
     */
    public FieldQueryParam(String fieldName, Object value) {
        this(fieldName, QueryComparison.equal, value);
    }

    public QueryComparison getQueryType() {
        return queryComparison;
    }

    public void setQueryType(QueryComparison queryComparison) {
        this.queryComparison = queryComparison;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
