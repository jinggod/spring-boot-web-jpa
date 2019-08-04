package com.frontsurf.springwebjpa.security.Filter;

import com.frontsurf.springwebjpa.security.CustomSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

/**
 * @Author xu.xiaojing
 * @Date 2018/10/8 9:49
 * @Email xu.xiaojing@frontsurf.com
 * @Description 自定义权限过滤器, 资源管理拦截器，在身份验证之后
 */
@Component
public class CustomAfterAuthenticationInterceptor extends AbstractSecurityInterceptor implements Filter {


    @Autowired
    CustomSecurityMetadataSource metadataSource;

    @Autowired
    CustomAccessDecisionManager accessDecisionManager;

    @PostConstruct
    public void init() {
        //设置权限决策器
        super.setAccessDecisionManager(accessDecisionManager);
    }

    /**
     * 资源服务器
     *
     * @return
     */
    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.metadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //设置权限决策器
        super.setAccessDecisionManager(accessDecisionManager);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }


    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {

        return FilterInvocation.class;
    }


}
