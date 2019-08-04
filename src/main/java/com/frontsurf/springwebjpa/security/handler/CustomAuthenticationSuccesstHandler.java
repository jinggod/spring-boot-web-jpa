package com.frontsurf.springwebjpa.security.handler;

import com.frontsurf.springwebjpa.security.handler.failcountblockade.FailureLoginCountHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author xu.xiaojing
 * @Date 2018/10/10 8:42
 * @Email xu.xiaojing@frontsurf.com
 * @Description 身份验证成功处理器
 */

@Component
public class CustomAuthenticationSuccesstHandler extends SimpleUrlAuthenticationSuccessHandler {

    static Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccesstHandler.class);
    @Autowired
    FailureLoginCountHandler failureLoginCountHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //super.onAuthenticationSuccess(request, response, authentication);
        logger.debug("身份验证成功");
        request.getRequestDispatcher("/login/success").forward(request, response);
        failureLoginCountHandler.successLogin(request.getParameter("username"));
        super.clearAuthenticationAttributes(request);
    }
}
