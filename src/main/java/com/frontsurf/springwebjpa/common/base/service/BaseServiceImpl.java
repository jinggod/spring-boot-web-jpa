package com.frontsurf.springwebjpa.common.base.service;

import com.frontsurf.springwebjpa.common.base.entity.BaseEntity;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.utils.BeanUtil;
import com.frontsurf.springwebjpa.common.utils.Reflections;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryParam;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryTypeEnum;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.common.utils.web.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/25 14:41
 * @Email xu.xiaojing@frontsurf.com
 * @Description 实现了扩展的自定义sql方法的方法
 */

public abstract class BaseServiceImpl<T extends BaseEntity<ID>, ID extends Serializable> implements BaseService<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> persistentClass;

    public BaseServiceImpl() {
        super();
        this.persistentClass = Reflections.getClassGenricType(getClass(), 0);
    }

    /**
     * 新增或更新
     *
     * 其实新增可以直接调用 repository 的save方法（preInsert方法可以进行拦截处理），但是更新需要则不行，因为先要查找旧的数据，然后判断哪些字段更新了，哪些字段没有更新，preupdate无法做到
     * @param t
     * @return
     * @throws DataException
     */
    @Override
    public T save(T t) throws DataException {
        if (t instanceof DataEntity) {
            DataEntity<ID> baseEntity = (DataEntity<ID>) t;
            Date current = new Date();
            if (baseEntity.getId() == null || baseEntity.getId().equals("")) {
                baseEntity.setCreateDate(LocalDateTime.now());
                baseEntity.setCreateBy(UserInfo.getCurrentUser());
                baseEntity.setCreateName(UserInfo.getUserName());
                return getDao().save(t);
            } else {
                Optional<T> t1 = getDao().findById(t.getId());
                if(!t1.isPresent()){
                    throw new DataException(Return.VALIDATION_ERROR,"更新失败，数据记录不存在");
                }
                baseEntity.setUpdateDate(LocalDateTime.now());
                baseEntity.setUpdateBy(UserInfo.getCurrentUser());
                BeanUtil.copyProperties(t, t1.get());
                return getDao().save(t1.get());
            }
        }
        return getDao().save(t);
    }

    /**
     * 批量新增
     *
     * @param entities
     * @return
     * @throws DataException
     */
    @Override
    public Iterable<T> save(Iterable<T> entities) throws DataException {
        for (T t : entities) {
            save(t);
        }
        return getDao().saveAll(entities);
    }

    @Override
    public void del(ID id) {
        getDao().deleteById(id);
    }

    @Override
    public void del(T t) {
        getDao().delete(t);
    }

    @Override
    public T findById(ID id) {
        return getDao().findById(id).orElse(null);
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll();
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


    /**
     * 动态参数的列表查询
     *
     * @param params 参数列表，必须是实体类的属性
     * @return
     */
    @Override
    public List<T> list(final List<QueryParam> params) {
        QueryParam param = new QueryParam("delFlag", QueryTypeEnum.equal, CommonConstant.DEL_FLAG_0);
        params.add(param);
        Specification spec = getSpecification(params);
        List<T> list = getDao().findAll(spec);
        return list;
    }

    /**
     * 动态的分页查询
     *
     * @param params   参数列表，必须是实体类的属性
     * @param pageable 分页参数
     * @return
     */
    @Override
    public Pager<T> list(final List<QueryParam> params, Pageable pageable) {
        QueryParam param = new QueryParam("delFlag", QueryTypeEnum.equal, CommonConstant.DEL_FLAG_0);
        params.add(param);
        Specification spec = getSpecification(params);
        Page<T> page = getDao().findAll(spec, pageable);
        return new Pager<T>(page.getContent(), page.isFirst(), page.isLast(), page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber() + 1);
    }

    /**
     * 动态的分页查询，并以 createDate降序排序
     *
     * @param params   参数列表，必须是实体类的属性
     * @param pageable 分页参数
     * @return
     */
    @Override
    public Pager<T> listWithDefaultSort(final List<QueryParam> params, Pageable pageable) {
        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createDate"));
        return this.list(params, pageable);
    }

    private Specification getSpecification(final Map<String, Object> params) {
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value == null || StringUtils.isBlank(value.toString())) {
                        continue;
                    }
                    String key = entry.getKey();
                    String[] arr = key.split(":");
                    Predicate predicate = getPredicate(arr[0], arr[1], value, root, cb);
                    list.add(predicate);
                }
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        };

        return spec;
    }

    public Specification getSpecification(final List<QueryParam> params) {
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                for (QueryParam param : params) {
                    Object value = param.getValue();
                    if (value == null || StringUtils.isBlank(value.toString())) {
                        continue;
                    }
                    Predicate predicate = getPredicate(param.getParamName(), param.getQueryType().name(), value, root, cb);
                    list.add(predicate);
                }
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        };

        return spec;
    }

    private Predicate getPredicate(String paramName, String queryTypeName, Object value,
                                   Root<T> root, CriteriaBuilder cb) {
        if (queryTypeName == null) {
            return cb.equal(root.get(paramName).as(value.getClass()), value);
        }
        if (QueryTypeEnum.equal.name().equals(queryTypeName)) {
            return cb.equal(root.get(paramName).as(value.getClass()), value);
        }
        if (QueryTypeEnum.like.name().equals(queryTypeName)) {
            return cb.like(root.get(paramName).as(String.class), String.format("%%%s%%", value));
        }
        if (QueryTypeEnum.ne.name().equals(queryTypeName)) {
            return cb.notEqual(root.get(paramName).as(value.getClass()), value);
        }
        if (QueryTypeEnum.lt.name().equals(queryTypeName)) {
            return getLessThanPredicate(paramName, queryTypeName, value, root, cb);
        }
        if (QueryTypeEnum.lte.name().equals(queryTypeName)) {
            return getLessThanOrEqualToPredicate(paramName, queryTypeName, value, root, cb);
        }
        if (QueryTypeEnum.gt.name().equals(queryTypeName)) {
            return getGreaterThanPredicate(paramName, queryTypeName, value, root, cb);
        }
        if (QueryTypeEnum.gte.name().equals(queryTypeName)) {
            return getGreaterThanOrEqualToPredicate(paramName, queryTypeName, value, root, cb);
        }

        throw new UnsupportedOperationException(String.format("不支持的查询类型[%s]", queryTypeName));
    }

    private Predicate getLessThanPredicate(String paramName, String queryTypeName, Object value,
                                           Root<T> root, CriteriaBuilder cb) {
        if (Integer.class == value.getClass()) {
            return cb.lessThan(root.get(paramName).as(Integer.class), (int) value);
        }
        if (Long.class == value.getClass()) {
            return cb.lessThan(root.get(paramName).as(Long.class), (long) value);
        }
        if (Double.class == value.getClass()) {
            return cb.lessThan(root.get(paramName).as(Double.class), (double) value);
        }
        if (Float.class == value.getClass()) {
            return cb.lessThan(root.get(paramName).as(Float.class), (float) value);
        }
        if (Timestamp.class == value.getClass()) {
            return cb.lessThan(root.get(paramName).as(Timestamp.class), (Timestamp) value);
        }
        if (Date.class == value.getClass()) {
            return cb.lessThan(root.get(paramName).as(Date.class), (Date) value);
        }
        return cb.lessThan(root.get(paramName).as(String.class), String.valueOf(value));
    }

    private Predicate getLessThanOrEqualToPredicate(String paramName, String queryTypeName,
                                                    Object value, Root<T> root, CriteriaBuilder cb) {
        if (Integer.class == value.getClass()) {
            return cb.lessThanOrEqualTo(root.get(paramName).as(Integer.class), (int) value);
        }
        if (Long.class == value.getClass()) {
            return cb.lessThanOrEqualTo(root.get(paramName).as(Long.class), (long) value);
        }
        if (Double.class == value.getClass()) {
            return cb.lessThanOrEqualTo(root.get(paramName).as(Double.class), (double) value);
        }
        if (Float.class == value.getClass()) {
            return cb.lessThanOrEqualTo(root.get(paramName).as(Float.class), (float) value);
        }
        if (Timestamp.class == value.getClass()) {
            return cb.lessThanOrEqualTo(root.get(paramName).as(Timestamp.class), (Timestamp) value);
        }
        if (Date.class == value.getClass()) {
            return cb.lessThanOrEqualTo(root.get(paramName).as(Date.class), (Date) value);
        }
        return cb.lessThanOrEqualTo(root.get(paramName).as(String.class), String.valueOf(value));
    }

    private Predicate getGreaterThanPredicate(String paramName, String queryTypeName,
                                              Object value, Root<T> root, CriteriaBuilder cb) {
        if (Integer.class == value.getClass()) {
            return cb.greaterThan(root.get(paramName).as(Integer.class), (int) value);
        }
        if (Long.class == value.getClass()) {
            return cb.greaterThan(root.get(paramName).as(Long.class), (long) value);
        }
        if (Double.class == value.getClass()) {
            return cb.greaterThan(root.get(paramName).as(Double.class), (double) value);
        }
        if (Float.class == value.getClass()) {
            return cb.greaterThan(root.get(paramName).as(Float.class), (float) value);
        }
        if (Timestamp.class == value.getClass()) {
            return cb.greaterThan(root.get(paramName).as(Timestamp.class), (Timestamp) value);
        }
        if (Date.class == value.getClass()) {
            return cb.greaterThan(root.get(paramName).as(Date.class), (Date) value);
        }
        return cb.greaterThan(root.get(paramName).as(String.class), String.valueOf(value));
    }

    private Predicate getGreaterThanOrEqualToPredicate(String paramName, String queryTypeName, Object value,
                                                       Root<T> root, CriteriaBuilder cb) {
        if (Integer.class == value.getClass()) {
            return cb.greaterThanOrEqualTo(root.get(paramName).as(Integer.class), (int) value);
        }
        if (Long.class == value.getClass()) {
            return cb.greaterThanOrEqualTo(root.get(paramName).as(Long.class), (long) value);
        }
        if (Double.class == value.getClass()) {
            return cb.greaterThanOrEqualTo(root.get(paramName).as(Double.class), (double) value);
        }
        if (Float.class == value.getClass()) {
            return cb.greaterThanOrEqualTo(root.get(paramName).as(Float.class), (float) value);
        }
        if (Timestamp.class == value.getClass()) {
            return cb.greaterThanOrEqualTo(root.get(paramName).as(Timestamp.class), (Timestamp) value);
        }
        if (Date.class == value.getClass()) {
            return cb.greaterThanOrEqualTo(root.get(paramName).as(Date.class), (Date) value);
        }
        return cb.greaterThanOrEqualTo(root.get(paramName).as(String.class), String.valueOf(value));
    }


}
