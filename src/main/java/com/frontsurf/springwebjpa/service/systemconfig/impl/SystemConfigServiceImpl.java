package com.frontsurf.springwebjpa.service.systemconfig.impl;

import com.frontsurf.springwebjpa.common.config.GlobalConfig;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.dao.systemconfig.SystemConfigRepository;
import com.frontsurf.springwebjpa.domain.systemconfig.SystemConfiguration;
import com.frontsurf.springwebjpa.service.systemconfig.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/23 15:17
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    SystemConfigRepository systemConfigRepository;


    @Override
    public List<SystemConfiguration> listAll() {
        return systemConfigRepository.findAll();
    }

    @Override
    public void update(String name, String value) throws DataException {
        SystemConfiguration systemConfiguration = systemConfigRepository.findByName(name);
        if (systemConfiguration == null) {
            throw new DataException(Return.VALIDATION_ERROR, "更新失败，此设置项不存在");
        }
        //校验值类型
        boolean b = this.verifyValueFormat(value, systemConfiguration.getType());
        if (!b) {
            throw new DataException(Return.VALIDATION_ERROR, "类型不一致");
        }
        systemConfiguration.setValue(value);

    }

    @Override
    public void updateSystemConfigInMerroy() {
        List<SystemConfiguration> configs = systemConfigRepository.findAll();
        for (SystemConfiguration config : configs) {
            switch (config.getType()) {
                case CommonConstant.VALUE_BOOLEAN:
                    Boolean bValue = Boolean.valueOf(config.getValue());
                    GlobalConfig.systemConfigMap.put(config.getName(), bValue);
                    break;
                case CommonConstant.VALUE_INTEGER:
                case CommonConstant.VALUE_SIGNED_INTEGER:
                    Integer iValue = Integer.valueOf(config.getValue());
                    GlobalConfig.systemConfigMap.put(config.getName(), iValue);
                    break;
                case CommonConstant.VALUE_DOUBLE:
                case CommonConstant.VALUE_SIGNED_DOUBLE:
                    Double dValue = Double.valueOf(config.getValue());
                    GlobalConfig.systemConfigMap.put(config.getName(), dValue);
                    break;
                default:
                    GlobalConfig.systemConfigMap.put(config.getName(), config.getValue());
                    break;
            }
        }
    }

    private boolean verifyValueFormat(String value, String type) {

        String regex = null;
        switch (type) {
            case CommonConstant.VALUE_BOOLEAN:
                regex = "^(true|false)$";
                break;
            case CommonConstant.VALUE_INTEGER:
                regex = "^[1-9]\\d*$";
                break;
            case CommonConstant.VALUE_DOUBLE:
                regex = "^[0-9]\\d*(\\.\\d+)*$";
                break;
            case CommonConstant.VALUE_SIGNED_INTEGER:
                regex = "^([+|-]?)([1-9]*)(\\d*)$";
            case CommonConstant.VALUE_SIGNED_DOUBLE:
                regex = "^([+|-]?)([1-9]*)(\\.\\d+)*$";
            default:
                break;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }


}
