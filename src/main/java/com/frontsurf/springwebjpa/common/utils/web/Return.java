package com.frontsurf.springwebjpa.common.utils.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 返回数据包装
 *
 * @author liu.zhiyang 2018/12/6 18:21
 * @Email liu.zhiyang@frontsurf.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Return implements Serializable {

    private static final long serialVersionUID = -5515899356745165159L;

    private Object data;
    private Integer code;
    private String message;

    public static final Integer COMMON_ERROR=400;
    public static final Integer VALIDATION_ERROR=400;
    public static final Integer UNAUTHORIZED_ERROR=403;
    public static final Integer UNAUTHENTICATED_ERROR=401;
    public static final Integer NOTFOUND_ERROR=404;


    public Return(Integer code) {
        this.code = code;
    }

    public Return(Object data, Integer code) {
        this.data = data;
        this.code = code;
    }

    public Return(Object data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public Return(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Return success(Object data) {
        return new Return(data, 200);
    }

    public static Return success() {
        return new Return(200);
    }

    public static Return success(String msg) {
        return new Return(200, msg);
    }

    public static Return success(Object data, String msg) {
        return new Return(data, 200, msg);
    }

    public static Return fail(String msg) {
        return new Return(COMMON_ERROR, msg);
    }

    public static Return failForValidationErr(String msg){
        return fail(VALIDATION_ERROR,msg);
    }

    public static Return failForUnauthorized(String msg){
        return fail(UNAUTHORIZED_ERROR,msg);
    }

    public static Return failForNotFound(String msg){
        return fail(NOTFOUND_ERROR,msg);
    }


    public static Return fail(Integer code, String msg) {
        return new Return(code, msg);
    }

    /**
     *
     * @param code 错误吗
     * @param msg 错误提示信息
     * @param detail 错误的详细信息
     * @return
     */
    public static Return fail(Integer code, String msg,Object detail) {
        return new Return(detail,code, msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
        setDefaultMsg();
    }

    private void setDefaultMsg() {
        if (HttpStatus.FORBIDDEN.value() == this.code) {
            this.code = 200;
            this.message = "权限不足";
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
