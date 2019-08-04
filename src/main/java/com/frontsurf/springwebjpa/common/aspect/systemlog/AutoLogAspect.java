package com.frontsurf.springwebjpa.common.aspect.systemlog;

import com.frontsurf.springwebjpa.common.utils.IPUtils;
import com.frontsurf.springwebjpa.common.utils.SpringContextUtils;
import com.frontsurf.springwebjpa.common.utils.web.UserInfo;
import com.frontsurf.springwebjpa.domain.user.User;
import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author xu.xiaojing
 * @Date 2018/8/27 12:37
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Component
@Aspect
public class AutoLogAspect {


    private boolean isupdate = false;

    private Object[] params;

    @Pointcut(value = "execution(* com.frontsurf.springwebjpa.controller..*.*(..))")
    public void pointcut() {
    }

    @Around(value = "@annotation(com.frontsurf.springwebjpa.common.aspect.systemlog.AutoLog)")
    public void beforeInvoke(ProceedingJoinPoint point) throws Throwable {

        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        this.saveSysLog(point,time);
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if (syslog != null) {
            //注解上的描述,操作日志内容
            sysLog.setLogContent(syslog.value());
            sysLog.setLogType(syslog.logType());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        Gson gson = new Gson();
        try {
            String params = gson.toJson(args);
            sysLog.setRequestParam(params);
        } catch (Exception e) {

        }

        //获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));

        //获取登录用户信息
        User sysUser = UserInfo.getCurrentUser();
        if (sysUser != null) {
            sysLog.setUserid(sysUser.getId());
            sysLog.setUsername(sysUser.getUsername());
        }
        //耗时
        sysLog.setCostTime(time);
        sysLog.setCreateTime(new Date());

        System.out.println("打印一条日志信息："+gson.toJson(sysLog));

    }

}
