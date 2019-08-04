package com.frontsurf.springwebjpa.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author xu.xiaojing
 * @Date 2019/5/28 9:50
 * @Email xu.xiaojing@frontsurf.com
 * @Description 有点特殊，这是一个调用链,最终必定调用SecurityContextLogoutHandler，所以不需要管如何退出，
 */

public class CustomLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        //释放资源锁
    }
}
