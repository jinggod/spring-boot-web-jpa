package com.frontsurf.springwebjpa.testbase.common;

import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/19 11:10
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public abstract class CustomSqlListVerify<T> {
    public abstract boolean verify(List<T> list);
}
