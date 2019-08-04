package com.frontsurf.springwebjpa.common.base.service;

import com.frontsurf.springwebjpa.common.base.entity.BaseEntity;
import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryParam;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/25 14:39
 * @Email xu.xiaojing@frontsurf.com
 * @Description 扩展了自定义sql的方法
 */

public interface BaseService<T extends BaseEntity<ID>, ID extends Serializable> {

    public abstract BaseRepository<T, ID> getDao();

    // ========================= 常用方法 =======================

    /**
     * 新增或更新
     * 主要用于更新，新增直接调用repo的方法即可
     */
    T save(T t) throws DataException;

    /**
     * 新增或更新
     * 注意数量不要太大，特别是数据迁移时不要使用该方法
     */
    Iterable<T> save(Iterable<T> entities) throws DataException;

    /**
     * 根据ID删除
     */
    void del(ID id);

    /**
     * 根据实体删除
     */
    void del(T t);

    /**
     * 根据ID查找对象
     */
    T findById(ID id);

    /**
     * 查找全部
     * @return
     */
    List<T> findAll();


    //  ======================= 自定义的方法 ====================================

    /**
     * 动态参数查询
     * @param params
     * @return
     */
    List<T> list(List<QueryParam> params);

    /**
     * 动态参数 ，且是分页查询
     * @param params
     * @param pageable
     * @return
     */
    Pager<T> list(List<QueryParam> params, Pageable pageable);

    /**
     * 传入构建好的sql，从而实现自定义sql查询
     * @param targetClass
     * @param sql
     * @param params
     * @return
     */
    T queryForObject(Class<T> targetClass, String sql, Object... params);

    /**
     * 自定义sql查询
     * @param sql
     * @param params
     * @return
     */
    Map<String,Object> queryForMap(String sql, Object... params);

    /**
     * 自定义sql查询
     * @param targetClass
     * @param sql
     * @param params
     * @return
     */
    List<T> queryForList(Class<T> targetClass, String sql, Object... params);

    /**
     * 自定义sql查询
     * @param sql
     * @param params
     * @return
     */
    List<Map<String,Object>> queryForListMap(String sql, Object... params);

    /**
     * 自定义sql的分页查询
     * @param targetClass
     * @param sql
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    Pager<T> queryForPager(Class<T> targetClass, String sql, int pageNum, int pageSize, Object... params);

    /**
     * 自定义sql的分页查询，但包含默认的排序条件
     * @param params
     * @param pageable
     * @return
     */
    Pager<T> listWithDefaultSort(List<QueryParam> params, Pageable pageable);
}
