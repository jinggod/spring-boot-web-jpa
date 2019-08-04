package com.frontsurf.springwebjpa.common.interceptor;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @Author xu.xiaojing
 * @Date 2019/3/27 16:24
 * @Email xu.xiaojing@frontsurf.com
 * @Description handle Servlet请求的拦截器
 */

@Component
@WebFilter(urlPatterns = "/**")
public class WebRequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
