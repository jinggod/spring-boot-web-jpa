package com.frontsurf.springwebjpa.common.base.service;


import com.frontsurf.springwebjpa.common.base.entity.BaseEntity;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.common.config.QueryConfig;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.utils.BeanUtil;
import com.frontsurf.springwebjpa.common.utils.Reflections;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.jpa.*;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.common.utils.web.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.frontsurf.springwebjpa.common.utils.web.Return.VALIDATION_ERROR;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/25 14:41
 * @Email xu.xiaojing@frontsurf.com
 * @Description 依赖于 BaseRepository，实现了最基本、通用的方法
 */
@SuppressWarnings("unchecked")
public abstract class BaseRepoServiceImpl<T extends BaseEntity<ID>, ID extends Serializable> implements BaseRepoService<T, ID> {

    public abstract BaseRepository<T, ID> getDao();

    private Class<T> persistentClass;

    public BaseRepoServiceImpl() {
        super();
        this.persistentClass = Reflections.getClassGenricType(getClass(), 0);
    }

    /**
     * 新增或更新
     * <p>
     * 其实新增可以直接调用 repository 的save方法（preInsert方法可以进行拦截处理），但是更新需要则不行，因为先要查找旧的数据，然后判断哪些字段更新了，哪些字段没有更新，preupdate无法做到
     *
     * @param t
     * @return
     * @throws DataException
     */
    @Override
    public T save(T t) throws DataException {
        if (t instanceof DataEntity) {
            DataEntity<ID> baseEntity = (DataEntity<ID>) t;
            if (baseEntity.getId() == null || baseEntity.getId().equals("")) {
                baseEntity.setCreateDate(LocalDateTime.now());
                baseEntity.setCreateBy(UserInfo.getCurrentUser());
                baseEntity.setCreateName(UserInfo.getUserName());
                return getDao().save(t);
            } else {
                return this.update(t);
            }
        }
        return getDao().save(t);
    }

    @Override
    public T update(T t) throws DataException {
        if (t instanceof DataEntity) {
            DataEntity<ID> baseEntity = (DataEntity<ID>) t;
            if (baseEntity.getId() == null || baseEntity.getId().equals("")) {
                throw new DataException(VALIDATION_ERROR, "数据异常，id不能为空");
            } else {
                Optional<T> t1 = getDao().findById(t.getId());
                if (!t1.isPresent()) {
                    throw new DataException(VALIDATION_ERROR, "更新失败，数据记录不存在");
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

    @Override
    public Pager<T> findAll(Pageable pageable) {
        if (DataEntity.class.isAssignableFrom(persistentClass)) {
            Pager<T> list = list(QueryConfig.defaultParams, pageable);
            return list;
        }
        Page<T> list = getDao().findAll(pageable);
        return new Pager<T>(list.getContent(), list.isFirst(), list.isLast(), list.getTotalElements(), list.getTotalPages(), list.getSize(), list.getNumber());
    }


    /**
     * 动态参数的列表查询
     *
     * @param params 参数列表，必须是实体类的属性
     * @return
     */
    @Override
    public List<T> list(final List<QueryParam> params) {
        QueryParam param = new FieldQueryParam("delFlag", QueryComparison.equal, CommonConstant.DEL_FLAG_0);
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
        QueryParam param = new FieldQueryParam("delFlag", QueryComparison.equal, CommonConstant.DEL_FLAG_0);
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


    public Specification getSpecification(final List<QueryParam> params) {
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                for (QueryParam param : params) {
                    List<Predicate> pres = getPredicate(param, root, cb);
                    if (pres != null)
                        list.addAll(pres);
                }
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        };

        return spec;
    }

    private List<Predicate> getPredicate(QueryParam param, From root, CriteriaBuilder cb) {
        List<Predicate> predicates = new LinkedList<>();
        if (param instanceof JoinQueryParam) {
            JoinQueryParam joinQueryParam = (JoinQueryParam) param;
            predicates = getPredicateFromJoinTable(joinQueryParam.getFieldName(), joinQueryParam.getJoinType(), joinQueryParam.getQueryParams(), root, cb);
        } else if (param instanceof FieldQueryParam) {
            FieldQueryParam fieldQueryParam = (FieldQueryParam) param;
            Object value = fieldQueryParam.getValue();
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                Predicate predicate = getPredicateFromField(fieldQueryParam.getFieldName(), fieldQueryParam.getQueryType().name(), value, root, cb);
                predicates.add(predicate);
            }
        } else if (param instanceof SubQueryParam) {
            // 不支持JOIN table的条件，而且也不合理
            SubQueryParam subQueryParam = (SubQueryParam) param;
            Predicate predicate = subQueryParam.getQueryParams().stream()
                    .map(p -> getPredicate(p, root, cb))
                    .filter(ps -> ps.size() > 0)
                    .map(ps -> {
                        if (ps.size() > 1)
                            return ps.stream().reduce((p1, p2) -> cb.and(p1, p2)).orElse(null);
                        else
                            return ps.get(0);
                    })
                    .reduce((p1, p2) -> {
                        if (subQueryParam.getQueryLinkEnum().equals(QueryLinkEnum.or))
                            return cb.or(p1, p2);
                        else
                            return cb.and(p1,p2);
                    }).orElse(null);
            if (predicate != null) {
                predicates.add(predicate);
            }
        }
        return predicates;
    }

    /**
     * 获取关联表的查询条件
     *
     * @param fieldName   实体的字段名称
     * @param joinTyp     关联类型
     * @param queryParams 关联表的查询参数列表
     * @param root        主体表
     * @param cb
     * @return
     */
    private List<Predicate> getPredicateFromJoinTable(String fieldName, JoinType joinTyp, List<QueryParam> queryParams, From root, CriteriaBuilder cb) {
        List<Predicate> predicateList = new LinkedList<>();
        Join<Object, Object> join = root.join(fieldName, joinTyp);
        if (queryParams != null) {
            for (QueryParam param : queryParams) {
                List<Predicate> predicates = getPredicate(param, join, cb);
                predicateList.addAll(predicates);
            }
        }
        return predicateList;
    }

    /**
     * 获取普通字段的查询条件
     *
     * @param paramName
     * @param queryTypeName
     * @param value
     * @param root          主体表
     * @param cb
     * @return
     */
    private Predicate getPredicateFromField(String paramName, String queryTypeName, Object value,
                                            From root, CriteriaBuilder cb) {
        if (queryTypeName == null) {
            return cb.equal(root.get(paramName).as(value.getClass()), value);
        }
        if (QueryComparison.equal.name().equals(queryTypeName)) {
            return cb.equal(root.get(paramName).as(value.getClass()), value);
        }
        if (QueryComparison.like.name().equals(queryTypeName)) {
            return cb.like(root.get(paramName).as(String.class), String.format("%%%s%%", value));
        }
        if (QueryComparison.ne.name().equals(queryTypeName)) {
            return cb.notEqual(root.get(paramName).as(value.getClass()), value);
        }
        if (QueryComparison.lt.name().equals(queryTypeName)) {
            return getLessThanPredicate(paramName, queryTypeName, value, root, cb);
        }
        if (QueryComparison.lte.name().equals(queryTypeName)) {
            return getLessThanOrEqualToPredicate(paramName, queryTypeName, value, root, cb);
        }
        if (QueryComparison.gt.name().equals(queryTypeName)) {
            return getGreaterThanPredicate(paramName, queryTypeName, value, root, cb);
        }
        if (QueryComparison.gte.name().equals(queryTypeName)) {
            return getGreaterThanOrEqualToPredicate(paramName, queryTypeName, value, root, cb);
        }

        throw new UnsupportedOperationException(String.format("不支持的查询类型[%s]", queryTypeName));
    }

    private Predicate getLessThanPredicate(String paramName, String queryTypeName, Object value,
                                           From root, CriteriaBuilder cb) {
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
                                                    Object value, From root, CriteriaBuilder cb) {
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
                                              Object value, From root, CriteriaBuilder cb) {
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
                                                       From root, CriteriaBuilder cb) {
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
