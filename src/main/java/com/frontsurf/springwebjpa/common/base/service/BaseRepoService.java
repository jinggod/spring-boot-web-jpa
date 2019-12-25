package com.frontsurf.springwebjpa.common.base.service;


import com.frontsurf.springwebjpa.common.base.entity.BaseEntity;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryParam;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/25 14:41
 * @Email xu.xiaojing@frontsurf.com
 * @Description 定义了最基本、通用的方法
 */
public interface BaseRepoService<T extends BaseEntity<ID>, ID extends Serializable> {

    // ========================= 常用方法 =======================
    /**
     * 新增或更新
     * 主要用于更新，新增直接调用repo的方法即可
     */
    @Deprecated
    T save(T t) throws DataException;

    /**
     * 更新，注意此方法非空才更新，使用了BeanUtil.copyProperties方法;
     * 如果需要全量更新，则直接使用 repo的save方法
     * @param t
     * @return
     * @throws DataException
     */
    T update(T t) throws DataException;

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
     *
     * @return
     */
    List<T> findAll();

    /**
     * 分页排序获取数据
     * 禁止使用该接口进行count操作
     * Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC,"id"));
     *
     * @param pageable
     * @return
     */
    Pager<T> findAll(Pageable pageable);


    //  ======================= 动态参数查询的方法：满足一定的动态sql需求 ====================================


    /**
     * 与比上面的两个方法等价，但更加清晰
     * @param params
     * @return
     */
    List<T> list(List<QueryParam> params);

    Pager<T> list(List<QueryParam> params, Pageable pageable);

    /**
     * 加了默认的查询条件，按createDate进行降序排序
     *
     * @param params
     * @param pageable
     * @return
     */
    Pager<T> listWithDefaultSort(List<QueryParam> params, Pageable pageable);


}
