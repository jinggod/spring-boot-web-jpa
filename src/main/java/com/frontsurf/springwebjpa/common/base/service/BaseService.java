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

public interface BaseService<T extends BaseEntity<ID>, ID extends Serializable> extends BaseRepoService<T,ID> {

    public abstract BaseRepository<T, ID> getDao();





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


}
