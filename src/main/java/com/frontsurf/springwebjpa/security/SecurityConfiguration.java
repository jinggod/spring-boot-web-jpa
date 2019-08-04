package com.frontsurf.springwebjpa.security;


import com.frontsurf.springwebjpa.security.Filter.CustomAccessDecisionManager;
import com.frontsurf.springwebjpa.security.Filter.CustomAfterAuthenticationInterceptor;
import com.frontsurf.springwebjpa.security.Filter.CustomBeforeAuthenticationFilter;
import com.frontsurf.springwebjpa.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author xu.xiaojing
 * @Date 2018/9/29 23:47
 * @Email xu.xiaojing@frontsurf.com
 * @Description 这是 Spring Security 必须配置的类,是Spring Security基础的配置类
 */

@Profile({"prod","pref","test"})
@Configuration
@EnableWebSecurity //
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UrlUserDetailsService urlUserDetailsService;

    @Autowired
    CustomAfterAuthenticationInterceptor urlFilterSecurityInterceptor;

    @Autowired
    CustomBeforeAuthenticationFilter customFilterBeforeAuthentication;

    @Autowired
    CustomAuthenticationSuccesstHandler successtHandler;

    @Autowired
    CustomAuthenticationFailHandler failHandler;
/*    @Resource(name = "customObjectPostProcessor")
    CustomObjectPostProcessor customObjectPostProcessor;*/

    @Autowired
    CustomSecurityMetadataSource metadataSource;

    @Autowired
    CustomAccessDecisionManager accessDecisionManager;

    /**
     * Http 安全
     * HttpSecurity用于提供一系列的Security默认的Filter，最终在WebSecurity对象中，组装到最终产生的springSecurityFilterChain 对象中去；
     * 用于配置资源URL的安全性,设置资源的过滤器，
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //登录设置
        http.formLogin().loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password").permitAll().successHandler(successtHandler).failureHandler(failHandler);
        //登出设置
        http.logout().logoutUrl("/logout").addLogoutHandler(new CustomLogoutHandler()).logoutSuccessHandler(new CustomLogoutSuccessHandler());
        //session设置
        http.sessionManagement()
                //超时跳转
                .invalidSessionUrl("/access/session/invalid")
                //最大并发session
                .maximumSessions(1000)
                // 是否阻止新的登录
                .maxSessionsPreventsLogin(true);
                // 并发session失效原因
                // .expiredSessionStrategy(sessionInformationExpiredStrategy)

        //关闭csrf，默认启用
        http.csrf().disable();
        //csrf对应的过滤器是 CsrfFilter
//        http.csrf().ignoringAntMatchers("/login/**", "/access/**");
        http.cors().disable();
        http.authorizeRequests().antMatchers("/login","/logout/**", "/access/**", "/static/**", "/error", "/login/**", "/collector/**", "/hicloud/**", "/actuator/**").permitAll();
        //华为云的四个接口
//        http.authorizeRequests().antMatchers("/hicloud","/hicloud/**","/hicloud/tenant/renewal").permitAll();
     /*   http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O obj) {
                obj.setSecurityMetadataSource(metadataSource);
                obj.setAccessDecisionManager(accessDecisionManager);
                return obj;
            }
        });*/
        //注意验证的顺序
        http.authorizeRequests().anyRequest().authenticated();
//        http.authorizeRequests().anyRequest().permitAll();
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()).authenticationEntryPoint(new CustomLoginUrlAuthenticationEntryPoint());
        //身份验证前的拦截器
        http.addFilterBefore(customFilterBeforeAuthentication, UsernamePasswordAuthenticationFilter.class);
        // 权限控制拦截器
        http.addFilterAfter(urlFilterSecurityInterceptor, FilterSecurityInterceptor.class);
    }

    /**
     * WEB 安全
     * WebSecurity 配置的是全局性，配置全局的过滤器，如 忽略某些资源，拒绝某些请求，设置调试模式 等
     * 所以 设置的过滤器 是要比 HttpSecurity 优先级高
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * AuthenticationManagerBuilder 是身份认证管理生成器，用于生成身份认证的创建机制
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        auth.userDetailsService(urlUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setUserDetailsService(urlUserDetailsService);
        //设置密码的加密方式，会对登陆时的密码参数进行加密后，再与数据库的密码进行判断，是否一致，也就是说不存储 明文的密码
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(daoAuthenticationProvider);
    }


    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
