package com.frontsurf.springwebjpa.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/1 23:16
 * @Email xu.xiaojing@frontsurf.com
 * @Description 访问拒绝处理器
 */

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletRequest.getRequestDispatcher("/access/denied?message=访问拒绝，没有此权限").forward(httpServletRequest, httpServletResponse);

    }
}
