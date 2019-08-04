package com.frontsurf.springwebjpa.controller.login;

import com.frontsurf.springwebjpa.common.utils.web.Return;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/11 8:51
 * @Email xu.xiaojing@frontsurf.com
 * @Description 处理请求访问的异常情况
 */

@RestController
@RequestMapping("/access")
public class AccessController {

    /**
     * 访问拒绝，没有相应的权限
     *
     * @return
     */
    @RequestMapping("/denied")
    public Return accessDenied(HttpServletResponse response,String message) {
        response.setStatus(403);
        //解決代理的跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");
        return Return.fail(Return.UNAUTHORIZED_ERROR, message);
    }


    /**
     * 访问拒绝，没有进行登录，无法进行身份验证
     *
     * @return
     */
    @RequestMapping("/unauthenticated")
    public Return unauthenticated(HttpServletResponse response) {
        response.setStatus(401);
        //解決代理的跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");
        return Return.fail(Return.UNAUTHENTICATED_ERROR, "访问失败，尚未登录，请先登录");
    }

    /**
     * session超时失效的处理
     * @param response
     * @return
     */
    @RequestMapping("/session/invalid")
    public Return sessionInvalid(HttpServletResponse response){
        response.setStatus(401);
        response.setHeader("session-timeout","true");
        return Return.fail(Return.VALIDATION_ERROR,"访问失败，Session已过期，请重新登录");
    }

}
