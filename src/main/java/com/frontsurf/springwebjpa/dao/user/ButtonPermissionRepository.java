package com.frontsurf.springwebjpa.dao.user;

import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.domain.user.ButtonPermission;
import org.springframework.stereotype.Repository;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/3 22:58
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Repository
public interface ButtonPermissionRepository extends BaseRepository<ButtonPermission, String> {
}
