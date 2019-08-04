package com.frontsurf.springwebjpa.common.utils.exception;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/1 20:35
 * @Email xu.xiaojing@frontsurf.com
 * @Description 运行时异常
 */

public class DataExceptionRuntime extends RuntimeException {

    private String errorMessage;
    private Integer code;


    public DataExceptionRuntime(String errorMessage) {
        super(errorMessage);
        this.code = 400;
        this.errorMessage = errorMessage;
    }

    public DataExceptionRuntime(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
