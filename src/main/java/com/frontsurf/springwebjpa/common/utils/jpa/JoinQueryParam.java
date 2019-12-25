package com.frontsurf.springwebjpa.common.utils.jpa;

import javax.persistence.criteria.JoinType;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/29 17:20
 * @Email xu.xiaojing@frontsurf.com
 * @Description 关联表查询参数
 */

public class JoinQueryParam extends QueryParam {
    /**
     * 关联查询的类型
     */
    JoinType joinType;

    /**
     * 关联表的额外查询参数，可参考教体医项目的学生列表查询
     */
    List<QueryParam> queryParams;

    public JoinQueryParam(String fieldName, JoinType joinType, List<QueryParam> queryParams) {
        super(fieldName);
        this.joinType = joinType;
        this.queryParams = queryParams;
    }

    public JoinQueryParam(String fieldName, JoinType joinType, QueryParam queryParam) {
        super(fieldName);
        this.joinType = joinType;
        this.queryParams = new LinkedList<>();
        this.queryParams.add(queryParam);
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public List<QueryParam> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<QueryParam> queryParams) {
        this.queryParams = queryParams;
    }
}
