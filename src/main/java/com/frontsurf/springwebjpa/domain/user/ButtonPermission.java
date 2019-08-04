package com.frontsurf.springwebjpa.domain.user;

import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import javax.persistence.*;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/3 21:37
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@SQLDelete(sql = "update button_permission set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@SQLDeleteAll(sql = "update button_permission set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@Table(name = "button_permission")
@Entity
public class ButtonPermission extends DataEntity<String> {

    private String name;
    private String code;
    private MenuPermission menuPermission;
    private String url;
    private String method;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    public MenuPermission getMenuPermission() {
        return menuPermission;
    }

    public void setMenuPermission(MenuPermission menuPermission) {
        this.menuPermission = menuPermission;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
