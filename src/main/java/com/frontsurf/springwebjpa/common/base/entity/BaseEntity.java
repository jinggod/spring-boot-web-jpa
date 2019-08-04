package com.frontsurf.springwebjpa.common.base.entity;

import com.frontsurf.springwebjpa.common.validate.Update;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 数据Entity基类
 */
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实体编号（唯一标识）
     */
    @NotNull(message = "id不能为空", groups = {Update.class})
    protected ID id;


    public BaseEntity() {

    }

    public BaseEntity(ID id) {
        this();
        this.id = id;
    }

    /**
     * 使用数据的自增策略（要求是Int类型，且数据库能支持）：
     * @GeneratedValue(strategy = IDENTITY)
     *
     * 使用Hibernate提供的 UUID生成器：
     *     @GenericGenerator(name = "uuid", strategy = "uuid")
     *     @GeneratedValue(generator = "uuid")
     * @return
     */
    @Id
    @GenericGenerator(name = "user-uuid", strategy = "com.frontsurf.springwebjpa.common.base.entity.CustomUUIDGenerator")
    @GeneratedValue(generator = "user-uuid")
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    /**
     * 插入之前执行方法，子类实现
     * 拦截每个JPA的插入操作
     */
    @PrePersist
    public abstract void preInsert();

    /**
     * 更新之前执行方法，子类实现
     * 拦截每个JPA的更新操作
     * <p>
     * 除了这两个，还有以下四个监听：
     *
     * @PostPersist  新增成功后执行
     * @PostRemove 删除成功后执行
     * @PostUpdate 更新后执行
     * @PreRemove  在删除前执行
     */
    @PreUpdate
    public abstract void preUpdate();


}
