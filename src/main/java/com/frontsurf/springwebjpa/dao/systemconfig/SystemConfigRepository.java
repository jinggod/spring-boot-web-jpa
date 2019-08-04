package com.frontsurf.springwebjpa.dao.systemconfig;

import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.domain.systemconfig.SystemConfiguration;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/26 17:06
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public interface SystemConfigRepository extends BaseRepository<SystemConfiguration,String> {
    SystemConfiguration findByName(String name);
}
