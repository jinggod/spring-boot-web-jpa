package com.frontsurf.springwebjpa.dao.excel;

import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.domain.excel.Device;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/24 15:55
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public interface DeviceRepository extends BaseRepository<Device,String> {
    Device findByCode(String code);
}
