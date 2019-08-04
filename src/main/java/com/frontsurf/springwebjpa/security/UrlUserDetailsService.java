package com.frontsurf.springwebjpa.security;


import com.frontsurf.springwebjpa.domain.user.ButtonPermission;
import com.frontsurf.springwebjpa.domain.user.MenuPermission;
import com.frontsurf.springwebjpa.domain.user.Role;
import com.frontsurf.springwebjpa.domain.user.User;
import com.frontsurf.springwebjpa.security.handler.failcountblockade.FailureLoginCountHandler;
import com.frontsurf.springwebjpa.security.model.UrlGrantedAuthority;
import com.frontsurf.springwebjpa.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @Author xu.xiaojing
 * @Date 2018/9/29 23:30
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Component
public class UrlUserDetailsService implements UserDetailsService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    FailureLoginCountHandler failureLoginCountHandler;

    @Autowired
    UserService userService;

    @Override
//    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //获取用户信息
        User user = userService.findByUsername(userName);

        //授权
        Set<UrlGrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Role> roles = user.getRoles();

        for (Role role : roles) {
            //处理菜单权限
            for (MenuPermission menuPermission : role.getMenuPermissions()) {
                if (StringUtils.isNotBlank(menuPermission.getUrl())) {
                    UrlGrantedAuthority urlGrantedAuthority = new UrlGrantedAuthority(menuPermission.getUrl(), menuPermission.getMethod(), menuPermission.getCode());
                    grantedAuthorities.add(urlGrantedAuthority);
                }
                System.out.println(menuPermission.getName());
            }
            //处理按钮权限
            for (ButtonPermission buttonPermission : role.getButtonPermissions()) {
                if (StringUtils.isNotBlank(buttonPermission.getUrl())) {
                    UrlGrantedAuthority urlGrantedAuthority = new UrlGrantedAuthority(buttonPermission.getUrl(), buttonPermission.getMethod(), buttonPermission.getCode());
                    grantedAuthorities.add(urlGrantedAuthority);
                }
            }
        }
        user.setAuthorities(new LinkedList<>(grantedAuthorities));
        return user;
    }

}


