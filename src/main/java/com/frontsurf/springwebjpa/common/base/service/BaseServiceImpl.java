package com.frontsurf.springwebjpa.common.base.service;

import com.frontsurf.springwebjpa.common.base.entity.BaseEntity;
import com.frontsurf.springwebjpa.common.utils.Reflections;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/25 14:41
 * @Email xu.xiaojing@frontsurf.com
 * @Description 实现了扩展的自定义sql方法的方法
 */

public abstract class BaseServiceImpl<T extends BaseEntity<ID>, ID extends Serializable> extends BaseRepoServiceImpl<T, ID> implements BaseService<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> persistentClass;

    public BaseServiceImpl() {
        super();
        this.persistentClass = Reflections.getClassGenricType(getClass(), 0);
    }

    /**
     * 查询一个对象
     *
     * @param targetClass
     * @param sql
     * @param params
     * @return
     */
    @Override
    public T queryForObject(Class<T> targetClass, String sql, Object... params) {
        Query query = entityManager.createNativeQuery(sql, targetClass);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return (T) query.getSingleResult();
    }

    /**
     * 查询一条数据，返回Map对象
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> queryForMap(String sql, Object... params) {
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        try {
            Object result = query.getSingleResult();
            return (Map<String, Object>) result;
        } catch (NoResultException e) {
            return null;
        }
    }


    /**
     * 查询一个列表
     *
     * @param targetClass
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<T> queryForList(Class<T> targetClass, String sql, Object... params) {
        Query query = entityManager.createNativeQuery(sql, targetClass);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return query.getResultList();
    }

    /**
     * 返回Map列表
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> queryForListMap(String sql, Object... params) {
        Query query = entityManager.createNativeQuery(sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        query.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        try {
            List result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return new LinkedList<>();
        }

    }


    /**
     * 分页查询
     *
     * @param tagetClass 目标类，不能是Map
     * @param sql        查询sql
     * @param countSql   计算总条数的sql
     * @param pageNum    页码
     * @param pageSize   页大小
     * @param params     参数
     * @return
     */
    public Pager<T> queryForPager(Class<T> tagetClass, String sql, String countSql, int pageNum, int pageSize, Object... params) {
        Query query = entityManager.createNativeQuery(sql, tagetClass);
        query.setFirstResult(pageNum * pageSize);
        query.setMaxResults(pageSize);
        Query countQuery = entityManager.createNativeQuery(countSql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
                countQuery.setParameter(i + 1, params[i]);
            }
        }
        List<T> resultList = query.getResultList();
        long total = ((BigInteger) countQuery.getSingleResult()).longValue();
        int totalPage = (int) (total / pageSize);
        if (total % pageSize != 0) {
            totalPage += 1;
        }
        pageNum += 1;
        Pager pager = new Pager(resultList, pageNum == 1, pageSize * pageNum >= total, total, totalPage, pageSize, pageNum);
        return pager;
    }

    /**
     * 快速的分页查询
     *
     * @param sql      原生sql
     * @param pageNum
     * @param pageSize
     * @param params   参数列表，数据库字段
     * @return
     */
    @Override
    public Pager<T> queryForPager(Class<T> targetClass, String sql, int pageNum, int pageSize, Object... params) {
        int index = StringUtils.indexOfIgnoreCase(sql, "FROM");
        String countSql = "SELECT COUNT(*) " + StringUtils.substring(sql, index);
        return this.queryForPager(targetClass, sql, countSql, pageNum, pageSize, params);
    }


}
