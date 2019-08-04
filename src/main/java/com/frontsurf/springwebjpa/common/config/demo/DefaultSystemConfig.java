package com.frontsurf.springwebjpa.common.config.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/8/4 10:20
 * @Email xu.xiaojing@frontsurf.com
 * @Description  感觉可能真的是没什么用，真的没有用，当个demo吧
 */

@Component
@ConfigurationProperties(prefix = "system.config")
public class DefaultSystemConfig {
    private Map<String, String> defaultConfig;

    private String scheduleTimeExpr;

    public String getScheduleTimeExpr() {
        return scheduleTimeExpr;
    }

    public void setScheduleTimeExpr(String scheduleTimeExpr) {
        this.scheduleTimeExpr = scheduleTimeExpr;
    }

    public Map<String, String> getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(Map<String, String> defaultConfig) {
        this.defaultConfig = defaultConfig;
    }
}
