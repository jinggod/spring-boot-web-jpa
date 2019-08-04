package com.frontsurf.springwebjpa.security.exception;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/21 0:53
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public enum  AutenticationExecptionType {

    /**
     * 账号登陆失败次数已达到阈值
     */
    FAIL_LOGIN_EXCEED_THRESHOLD,

    /**
     * 用户不存在
     */
    USER_NOT_FOUND,

    /**
     * 租户不存在
     */
    TENENT_NOT_FOUND,

    /**
     * 租户已过期
     */
    TENEN_PAST_DUE

}
