package com.frontsurf.springwebjpa.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/2 14:41
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.getRequestDispatcher("/login/logout/success").forward(httpServletRequest,httpServletResponse);
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
    }
}
