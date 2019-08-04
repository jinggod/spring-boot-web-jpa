package com.frontsurf.springwebjpa.service.user;

import com.frontsurf.springwebjpa.common.base.service.BaseService;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.ListRequestParam;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.domain.user.Role;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/24 1:35
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public interface RoleService extends BaseService<Role, String> {
    Pager<Role> listRolePager(ListRequestParam requestParam, Role role);

    void saveRole(Role role) throws DataException;

    void updateRole(Role role) throws DataException;
}
