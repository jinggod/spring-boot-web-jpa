package com.frontsurf.springwebjpa.common.utils.jpa;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/29 17:29
 * @Email xu.xiaojing@frontsurf.com
 * @Description 动态查询参数
 */

public abstract class QueryParam {

    /**
     * 实体的里面的字段名，非数据库字段名
     */
    private String fieldName;

    public QueryParam(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
