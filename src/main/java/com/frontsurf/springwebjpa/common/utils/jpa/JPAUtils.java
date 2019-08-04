package com.frontsurf.springwebjpa.common.utils.jpa;

import com.frontsurf.springwebjpa.common.utils.Reflections;

import javax.persistence.EntityNotFoundException;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/30 17:18
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class JPAUtils {

    /**
     * 用于触发对象的某个懒加载字段
     *
     * @param obj       实例
     * @param fieldName 所需要触发的懒加载字段
     * @param <T>
     * @throws Exception
     */
    public static <T> void lazyLoad(Object obj, String fieldName) {
        try {
            Object fieldObj = Reflections.invokeGetter(obj, fieldName);
            if (fieldObj != null) {
                fieldObj.toString();
            }
        } catch (EntityNotFoundException e) {
            Reflections.invokeSetter(obj, fieldName, null);
        }
    }
}
