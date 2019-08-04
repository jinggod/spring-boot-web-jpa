package com.frontsurf.springwebjpa.security.handler.failcountblockade;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/20 23:29
 * @Email xu.xiaojing@frontsurf.com
 * @Description 对失败次数不处理，即账号不会被锁定
 */

public class NoOpsFailureLoginCountHandler extends FailureLoginCountHandler {
    @Override
    public void isAccountLocked(String userName) {
    }

    @Override
    public void failLogin(String userName) {
    }

    @Override
    public void successLogin(String userName) {
    }
}
