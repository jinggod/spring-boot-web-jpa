package com.frontsurf.springwebjpa.security.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/21 0:50
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class CustomAutenticationExecption extends UsernameNotFoundException {

    /**
     * 身份校验异常类型
     */
    private AutenticationExecptionType type;

    public CustomAutenticationExecption(AutenticationExecptionType type,String msg) {
        super(msg);
        this.type = type;
    }

    public CustomAutenticationExecption(AutenticationExecptionType type,String msg, Throwable t) {
        super(msg, t);
        this.type = type;
    }

    public AutenticationExecptionType getType() {
        return type;
    }

    public void setType(AutenticationExecptionType type) {
        this.type = type;
    }
}
