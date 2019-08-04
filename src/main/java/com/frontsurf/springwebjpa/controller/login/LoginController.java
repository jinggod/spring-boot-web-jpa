package com.frontsurf.springwebjpa.controller.login;

import com.frontsurf.springwebjpa.common.config.GlobalConfig;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.common.utils.web.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author xu.xiaojing
 * @Date 2019/3/29 16:34
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@RestController
@RequestMapping("/login")
public class LoginController {


    @Autowired
    GlobalConfig globalConfig;

    /**
     * 登录成功
     *
     * @return
     */
    @RequestMapping("/success")
    public Return loginSucess(HttpServletResponse response) {
        //解決代理的跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");
        return Return.success("登录成功");
    }

    /**
     * 登录失败
     *
     * @return
     */
    @RequestMapping("/fail")
    public Return loginFail(HttpServletRequest request, HttpServletResponse response, String message) {

        request.getLocalPort();
        //解決代理的跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");
        return Return.fail(message);
    }

    /**
     * 登出成功的处理，用户已退出，勿进行用户相关的操作
     *
     * @param response
     * @return
     */
    @RequestMapping("/logout/success")
    public Return logoutSuccessProcess(HttpServletResponse response) {

        //解決代理的跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return Return.fail(Return.COMMON_ERROR, "尚未登出，请重试");
        }
        return Return.success("登出成功");
    }

    /**
     * 获取当前用户信息
     *
     * @param response
     * @return
     */
    @GetMapping("/user")
    public Return getCurrentUser(HttpServletResponse response) {

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return Return.fail(Return.UNAUTHENTICATED_ERROR, "尚未登录，请登录");
        }
        return Return.success(UserInfo.getCurrentUser());
    }

    /**
     * spring security开启了 csrf防御后，才能获取到token
     * 开启了csrf后，每个post请求都需要获取这个token
     *
     * @param token
     * @return
     */
    @GetMapping("/token")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }


}
