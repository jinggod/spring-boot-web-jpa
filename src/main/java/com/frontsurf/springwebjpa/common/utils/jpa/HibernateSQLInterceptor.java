package com.frontsurf.springwebjpa.common.utils.jpa;

import org.hibernate.EmptyInterceptor;

/**
 * 拦截Hibernate的方法，只实现onPrepareStatement改写SQL
 * SpringBoot 配置:Interceptor: spring.jpa.properties.hibernate.ejb.interceptor.session_scoped
 *
 * @author YanpHu
 */
public class HibernateSQLInterceptor extends EmptyInterceptor {

    /**
     *
     */
    private static final long serialVersionUID = -8497637816391956683L;

    private static ThreadLocal<Integer> PARKID_THREADLOCAL = new ThreadLocal<>();

    public static void setParkID(Integer parkID) {
        PARKID_THREADLOCAL.set(parkID);
    }

    public static void remove() {
        PARKID_THREADLOCAL.remove();
    }

    @Override
    public String onPrepareStatement(String sql) {

        System.out.println("-----Hibernate sql拦截器：" + sql);
        return sql;
    }
}
