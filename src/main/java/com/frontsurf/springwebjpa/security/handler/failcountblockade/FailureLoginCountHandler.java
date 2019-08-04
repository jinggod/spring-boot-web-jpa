package com.frontsurf.springwebjpa.security.handler.failcountblockade;

import com.frontsurf.springwebjpa.security.exception.CustomAutenticationExecption;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/20 23:23
 * @Email xu.xiaojing@frontsurf.com
 * @Description 登陆失败次数处理器
 */

public abstract class FailureLoginCountHandler {

    /**
     * 账号是否被锁住
     *
     * @param userName 用户ID
     * @return
     */
    public abstract void isAccountLocked(String userName) throws CustomAutenticationExecption;

    public abstract void failLogin(String userName);

    public abstract void successLogin(String userName);
}
