package com.frontsurf.springwebjpa.service.systemconfig;

import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.domain.systemconfig.SystemConfiguration;

import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/23 15:17
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public interface SystemConfigService {
    List<SystemConfiguration> listAll();

    void update(String name, String value) throws DataException;

    void updateSystemConfigInMerroy();
}
