package com.frontsurf.springwebjpa.dao.user;

import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.domain.user.MenuPermission;
import org.springframework.stereotype.Repository;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/3 23:00
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@Repository
public interface MenuPermissionRepository extends BaseRepository<MenuPermission, String> {
}
