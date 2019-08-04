package com.frontsurf.springwebjpa.security;

import com.frontsurf.springwebjpa.dao.user.ButtonPermissionRepository;
import com.frontsurf.springwebjpa.dao.user.MenuPermissionRepository;
import com.frontsurf.springwebjpa.domain.user.ButtonPermission;
import com.frontsurf.springwebjpa.domain.user.MenuPermission;
import com.frontsurf.springwebjpa.security.model.UrlConfigAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/2 0:06
 * @Email xu.xiaojing@frontsurf.com
 * @Description 资源服务器，与 CustomAccessDecisionManager配合使用
 */
@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    MenuPermissionRepository menuPermissionRepository;

    @Autowired
    ButtonPermissionRepository buttonPermissionRepository;

    Map<String, Collection<ConfigAttribute>> medataSources = new HashMap<>();

    /**
     * 获取一个资源所需要的权限集合
     *
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) o;
        if (medataSources.size() <= 0) {
            getAllConfigAttributes();
        }
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : medataSources.entrySet()) {
            String uri = entry.getKey();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(uri);
            if (requestMatcher.matches(fi.getHttpRequest())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取所有资源的所需要的权限
     *
     * @return
     */
    @Transactional
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        List<MenuPermission> menuPermissions = menuPermissionRepository.findAll();
        List<ButtonPermission> buttonPermissions = buttonPermissionRepository.findAll();
        for (MenuPermission menuPermission : menuPermissions) {
            if (StringUtils.isNotBlank(menuPermission.getUrl())) {
                ArrayList<ConfigAttribute> configAttributes = new ArrayList<>();
                UrlConfigAttribute urlConfigAttribute = new UrlConfigAttribute(menuPermission.getUrl(), menuPermission.getMethod());
                configAttributes.add(urlConfigAttribute);
                medataSources.put(menuPermission.getUrl(), configAttributes);
            }
        }

        for (ButtonPermission buttonPermission : buttonPermissions) {
            if (StringUtils.isNotBlank(buttonPermission.getUrl())) {
                ArrayList<ConfigAttribute> configAttributes = new ArrayList<>();
                UrlConfigAttribute urlConfigAttribute = new UrlConfigAttribute(buttonPermission.getUrl(), buttonPermission.getMethod());
                configAttributes.add(urlConfigAttribute);
                medataSources.put(buttonPermission.getUrl(), configAttributes);
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }


}
