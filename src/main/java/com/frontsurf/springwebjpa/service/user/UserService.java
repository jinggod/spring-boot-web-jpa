package com.frontsurf.springwebjpa.service.user;

import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.common.base.service.BaseService;
import com.frontsurf.springwebjpa.common.utils.web.ListRequestParam;
import com.frontsurf.springwebjpa.domain.user.User;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/24 1:35
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public interface UserService extends BaseService<User, String> {
    User findByUsername(String userName);

    Pager<User> listUserPager(ListRequestParam listRequestParam, User user);

    User findByIdUseSql(String id);

    void saveUser(User user) throws DataException;

    void updateUser(User user) throws DataException;
}
