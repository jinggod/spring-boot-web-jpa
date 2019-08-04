package com.frontsurf.springwebjpa.security.Filter;

import com.frontsurf.springwebjpa.common.config.GlobalConfig;
import com.frontsurf.springwebjpa.common.utils.RSAUtils;
import com.frontsurf.springwebjpa.security.handler.failcountblockade.FailureLoginCountHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/21 10:15
 * @Email xu.xiaojing@frontsurf.com
 * @Description 在身份检验前进行拦截，可处理一些验证码、登陆失败次数的账号锁住
 */

@Component
public class CustomBeforeAuthenticationFilter implements Filter {

    @Autowired
    FailureLoginCountHandler failureLoginCountHandler;

    @Autowired
    GlobalConfig globalConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String username = servletRequest.getParameter("username");
        String password = servletRequest.getParameter("password");
        String encrypt = servletRequest.getParameter("encrypt");
        if (username != null) {
            //处理账号封锁
            failureLoginCountHandler.isAccountLocked(username);
        }
        HttpServletRequestExtend requestExtend = new HttpServletRequestExtend((HttpServletRequest) servletRequest);
        //默认是加密的
        if ((encrypt == null || encrypt.equals("1")) && StringUtils.isNotBlank(password)) {
            try {
                password = RSAUtils.decryptDataOnJava(password, globalConfig.privateKey);
                System.out.println(password);
                //设置密码为解密后的密码
                requestExtend.addParameter("password", password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //
        // 从 HTTP 头中取得 Referer 值
        String referer = ((HttpServletRequest) servletRequest).getHeader("Referer");
        System.out.println("referer：" + referer);
        filterChain.doFilter(requestExtend, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
