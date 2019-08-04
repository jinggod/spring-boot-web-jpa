package com.frontsurf.springwebjpa.controller.systemconfig;

import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.domain.systemconfig.SystemConfiguration;
import com.frontsurf.springwebjpa.service.systemconfig.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/26 16:36
 * @Email xu.xiaojing@frontsurf.com
 * @Description 系统配置
 */

@RestController
@RequestMapping("/system_config")
public class SystemConfigController {

    @Autowired
    SystemConfigService systemConfigService;

    @GetMapping("/list")
    public Return listAll() {
        List<SystemConfiguration> systemConfigurations = systemConfigService.listAll();
        return Return.success(systemConfigurations);
    }

    @PostMapping("/update")
    public Return update(String name, String value) {

        try {
            systemConfigService.update(name, value);
        } catch (DataException e) {
            e.printStackTrace();
            return Return.fail(e.getCode(), e.getErrorMessage());
        }
        return Return.success("更新成功");
    }
}
