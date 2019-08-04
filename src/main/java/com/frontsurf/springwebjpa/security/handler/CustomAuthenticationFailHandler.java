package com.frontsurf.springwebjpa.security.handler;

import com.frontsurf.springwebjpa.security.exception.AutenticationExecptionType;
import com.frontsurf.springwebjpa.security.exception.CustomAutenticationExecption;
import com.frontsurf.springwebjpa.security.handler.failcountblockade.FailureLoginCountHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author xu.xiaojing
 * @Date 2018/10/10 9:25
 * @Email xu.xiaojing@frontsurf.com
 * @Description 身份验证失败处理器
 */
@Component
public class CustomAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    FailureLoginCountHandler failureLoginCountHandler;

    static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.debug("身份验证失败");
        String message = exception.getMessage();
        if (message.equalsIgnoreCase("Bad credentials")) {
            message = "用户名或密码错误！";
        }
        //处理登陆失败的计数
        if (exception instanceof CustomAutenticationExecption) {
            CustomAutenticationExecption customAutenticationExecption = (CustomAutenticationExecption) exception;
            if (!customAutenticationExecption.getType().equals(AutenticationExecptionType.FAIL_LOGIN_EXCEED_THRESHOLD)) {
                String username = request.getParameter("username");
                failureLoginCountHandler.failLogin(username);
            }
        } else {
            String username = request.getParameter("username");
            failureLoginCountHandler.failLogin(username);
        }
        request.getRequestDispatcher("/login/fail?message=" + message).forward(request, response);
    }
}
