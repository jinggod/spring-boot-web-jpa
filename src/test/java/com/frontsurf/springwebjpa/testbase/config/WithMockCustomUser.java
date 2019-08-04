package com.frontsurf.springwebjpa.testbase.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author xu.xiaojing
 * @Date 2019/5/11 12:51
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "rob";

    String name() default "Rob Winch";
}
