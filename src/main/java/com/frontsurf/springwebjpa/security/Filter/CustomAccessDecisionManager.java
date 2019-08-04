package com.frontsurf.springwebjpa.security.Filter;

import com.frontsurf.springwebjpa.security.model.UrlConfigAttribute;
import com.frontsurf.springwebjpa.security.model.UrlGrantedAuthority;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @Author xu.xiaojing
 * @Date 2018/10/8 9:57
 * @Email xu.xiaojing@frontsurf.com
 * @Description 在身份验证后的拦截器调用的，是一个 权限决策器，与 结合使用
 */

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {


    /**
     *
     * @param authentication 访问者
     * @param object
     * @param configAttributes  此资源所需要的 权限集合
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

      if(true)
          return;

        // 判断用户是否拥有权限
        if (configAttributes != null) {
            for (ConfigAttribute configAttribute : configAttributes) {
                UrlConfigAttribute urlConfigAttribute = (UrlConfigAttribute) configAttribute;
                for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                    UrlGrantedAuthority urlGrantedAuthority = (UrlGrantedAuthority) grantedAuthority;
                    if (urlGrantedAuthority.getPermissionUrl().equals(urlConfigAttribute.getUri())) {
                        return;
                    }
                }
            }
        }

        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    private boolean matchers(String url, HttpServletRequest request) {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(url);
        if (matcher.matches(request)) {
            return true;
        }
        return false;
    }
}
