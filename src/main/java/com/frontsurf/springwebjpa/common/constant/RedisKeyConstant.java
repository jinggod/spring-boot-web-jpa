package com.frontsurf.springwebjpa.common.constant;

/**
 * @Author xu.xiaojing
 * @Date 2019/8/3 10:40
 * @Email xu.xiaojing@frontsurf.com
 * @Description key值前缀
 */

public interface RedisKeyConstant {

    /**
     * 各类手机验证码
     * 家长手机验证码:PARENT_VERIFY
     * 个人用户登录验证码：LOGIN_VERIFY
     * 个人用户注册验证码：REGISTER_VERIFY
     */
    String PHONE_PARENT_VERIFY = "smscode:parent";
    String PHONE_LOGIN_VERIFY = "smscode:login";
    String PHONE_REGISTER_VERIFY = "smscode:register";

    /**
     * 个人用户第一次登陆的key值
     */
    String LOGIN_FIRST_TIME = "fisrt:time:login:";

}
