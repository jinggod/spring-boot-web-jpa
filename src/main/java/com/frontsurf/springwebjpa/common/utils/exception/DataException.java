package com.frontsurf.springwebjpa.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author xu.xiaojing
 * @Date 2019/3/4 14:16
 * @Email xu.xiaojing@frontsurf.com
 * @Description 自定义的异常类
 */

public class DataException extends Exception {

    Logger logger = LoggerFactory.getLogger(DataException.class);

    //错误提示信息
    private String errorMessage;
    //错误编码
    private Integer code;
    // 具体的错误信息
    private Object detail;


    public DataException(String errorMessage) {
        super(errorMessage);
        this.code = 400;
        this.errorMessage = errorMessage;
    }

    public DataException(Integer code ,String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public DataException( Integer code, String errorMessage, Object detail) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.code = code;
        this.detail = detail;
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

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        logger.debug("业务逻辑出现异常：",this);
    }
}
