package com.frontsurf.springwebjpa.common.base.entity;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/27 9:29
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

/**
 * 自定义UUID生成器
 *
 * @author sevenlin
 */
public class CustomUUIDGenerator extends UUIDGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        return super.generate(session, object);
    }


}

