package com.frontsurf.springwebjpa.testbase.common;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/19 10:46
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public abstract class CustomSqlOneVerify<T> {

    public abstract boolean verify(T t);
}
