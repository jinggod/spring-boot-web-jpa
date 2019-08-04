package com.frontsurf.springwebjpa.security.model;

import org.springframework.security.access.ConfigAttribute;

/**
 * @Author xu.xiaojing
 * @Date 2018/10/9 8:49
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class UrlConfigAttribute implements ConfigAttribute {

    private String uri;

    private String method;

    public UrlConfigAttribute(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getAttribute() {
        return this.uri + ";" + this.method;
    }
}
