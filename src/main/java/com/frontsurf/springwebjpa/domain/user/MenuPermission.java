package com.frontsurf.springwebjpa.domain.user;

import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import javax.persistence.*;
import java.util.Set;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/3 21:30
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@SQLDelete(sql = "update menu_permission set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@SQLDeleteAll(sql = "update menu_permission set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@Table(name = "menu_permission")
@Entity
public class MenuPermission extends DataEntity<String> {

    private String name;
    private String code;
    private MenuPermission parentMenuPer;
    private String url;
    private String method;

    Set<MenuPermission> childMeunPers;

    Set<ButtonPermission> buttonPermissions;


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
    @JoinColumn(name = "parent_id")
    public MenuPermission getParentMenuPer() {
        return parentMenuPer;
    }

    public void setParentMenuPer(MenuPermission parentMenuPer) {
        this.parentMenuPer = parentMenuPer;
    }

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "menuPermission")
    public Set<ButtonPermission> getButtonPermissions() {
        return buttonPermissions;
    }

    public void setButtonPermissions(Set<ButtonPermission> buttonPermissions) {
        this.buttonPermissions = buttonPermissions;
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

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "parentMenuPer")
    public Set<MenuPermission> getChildMeunPers() {
        return childMeunPers;
    }

    public void setChildMeunPers(Set<MenuPermission> childMeunPers) {
        this.childMeunPers = childMeunPers;
    }
}
