package com.frontsurf.springwebjpa.security.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * @Author xu.xiaojing
 * @Date 2018/10/8 0:11
 * @Email xu.xiaojing@frontsurf.com
 * @Description 自定义的权限类，精确到方法
 */

public class UrlGrantedAuthority implements GrantedAuthority {

    private String permissionUrl;
    private String method;
    private String code;

    public UrlGrantedAuthority(String permissionUrl, String method, String code) {
        this.permissionUrl = permissionUrl;
        this.method = method;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getAuthority() {
        return this.permissionUrl + ";" + this.method;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlGrantedAuthority that = (UrlGrantedAuthority) o;
        return Objects.equals(permissionUrl, that.permissionUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(permissionUrl);
    }
}
