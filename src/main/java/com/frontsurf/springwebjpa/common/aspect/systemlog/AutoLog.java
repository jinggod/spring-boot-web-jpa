package com.frontsurf.springwebjpa.common.aspect.systemlog;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @Author scott
 * @email jeecgos@163.com
 * @Date 2019年1月14日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

    /**
     * 日志内容
     *
     * @return
     */
    String value() default "";

    /**
     * 日志类型
     *
     * @return 登陆、登出、增、删、改、查
     */
    LogType logType() default LogType.VIEW;
}
