package com.frontsurf.springwebjpa.common.task;

import com.frontsurf.springwebjpa.service.systemconfig.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/19 14:55
 * @Email xu.xiaojing@frontsurf.com
 * @Description 定时任务，所有的定时任务放此处，进行统一管理
 */

@Component
public class ScheduleTask {

    static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    SystemConfigService systemConfigService;

    /**
     * 定时将系统设置更新到内存
     */
    @Scheduled(cron = "${system.config.scheduleTimeExpr}")
    public void timmingTask() {
        systemConfigService.updateSystemConfigInMerroy();
    }
}
