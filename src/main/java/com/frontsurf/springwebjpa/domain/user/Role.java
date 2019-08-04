package com.frontsurf.springwebjpa.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.domain.user.ButtonPermission;
import com.frontsurf.springwebjpa.domain.user.MenuPermission;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import javax.persistence.*;
import java.util.Set;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/23 14:40
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@SQLDelete(sql = "update role set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@SQLDeleteAll(sql = "update role set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@Entity
@Table(name = "role")
public class Role extends DataEntity<String> {

    private String name;

    Set<MenuPermission> menuPermissions;

    Set<ButtonPermission> buttonPermissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Cascade 要注意，选择合适的级联关系，特别是 ALL、PERSISIT
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "role_menu_permission"
            , joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
    public Set<MenuPermission> getMenuPermissions() {
        return menuPermissions;
    }

    public void setMenuPermissions(Set<MenuPermission> menuPermissions) {
        this.menuPermissions = menuPermissions;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "role_button_permission"
            , joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "button_id", referencedColumnName = "id")})
    public Set<ButtonPermission> getButtonPermissions() {
        return buttonPermissions;
    }

    public void setButtonPermissions(Set<ButtonPermission> buttonPermissions) {
        this.buttonPermissions = buttonPermissions;
    }

}
