package com.frontsurf.springwebjpa.dao.user;

import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.domain.user.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/24 1:33
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Repository
public interface RoleRepository extends BaseRepository<Role,String> {
   Role findByName(String admin);
}
