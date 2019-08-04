package com.frontsurf.springwebjpa.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/2 11:04
 * @Email xu.xiaojing@frontsurf.com
 * @Description 没有进行身份验证被拒后处理器
 */

public class CustomLoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        httpServletRequest.getRequestDispatcher("/access/unauthenticated").forward(httpServletRequest,httpServletResponse);
    }
}
