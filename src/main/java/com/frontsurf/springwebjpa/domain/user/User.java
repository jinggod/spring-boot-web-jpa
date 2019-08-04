package com.frontsurf.springwebjpa.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.validate.Save;
import com.frontsurf.springwebjpa.domain.user.Role;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/23 14:40
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@SQLDelete(sql = "update user set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@SQLDeleteAll(sql = "update user set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
//@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@Table(name = "user")
@Entity
public class User extends DataEntity<String> implements UserDetails {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {Save.class})
    @Column(length = 50,unique = true)
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {Save.class})
//    @Pattern(regexp = "^[a-zA-Z0-9]{0,10}", groups = {Save.class, Update.class},message = "密码为字母和数字组合，长度不能超过10")
    private String password;

    /**
     * 权限列表
     */
    private List<? extends GrantedAuthority> authorities;


    Set<Role> roles;

    /**
     * OnetoMany、ManyToMany的JPA是不支持 饿加载
     * @return
     */
    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return this.password;
    }


    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setAuthorities(List<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @JsonIgnore
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @JsonIgnore
    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }
}
