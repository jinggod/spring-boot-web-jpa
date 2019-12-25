package com.frontsurf.springwebjpa.service.role.impl;

import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.common.base.service.BaseServiceImpl;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.jpa.FieldQueryParam;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryParam;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryComparison;
import com.frontsurf.springwebjpa.common.utils.web.ListRequestParam;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.dao.user.RoleRepository;
import com.frontsurf.springwebjpa.domain.user.Role;
import com.frontsurf.springwebjpa.service.user.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/24 1:36
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, String> implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public BaseRepository<Role, String> getDao() {
        return roleRepository;
    }

    @Override
    public Pager<Role> listRolePager(ListRequestParam requestParam, Role role) {
        Sort sort = null;
        if (requestParam.getOrderBy() != null) {
            Sort.Direction direction = Sort.Direction.ASC;
            if (requestParam.getDesc() != null && requestParam.getDesc() == 1) {
                direction = Sort.Direction.DESC;
            }
            switch (requestParam.getOrderBy()) {
                case 0:
                    sort = new Sort(direction, "name");
            }
        }
        //sort不能为null，或者不传
        PageRequest pageRequest;
        if (sort != null) {
            pageRequest = PageRequest.of(requestParam.getPageNum(), requestParam.getPageSize(), sort);
        } else {
            pageRequest = PageRequest.of(requestParam.getPageNum(), requestParam.getPageSize());
        }

        //模糊查询
        List<QueryParam> params = new LinkedList<>();
        if (role != null) {
            if (StringUtils.isNotBlank(role.getName())) {
                QueryParam param = new FieldQueryParam("name", QueryComparison.like, role.getName());
                params.add(param);
            }
        }
        Pager<Role> list = this.listWithDefaultSort(params, pageRequest);
        return list;
    }

    @Override
    public void saveRole(Role role) throws DataException {
        //判断角色名称是否已存在
        Role existRole = roleRepository.findByName(role.getName());
        if (existRole != null) {
            throw new DataException(Return.VALIDATION_ERROR, "新增失败，角色名称已存在");
        }
        this.save(role);
    }

    @Override
    public void updateRole(Role role) throws DataException {
        //判断角色名称是否已存在
        Role existRole = roleRepository.findByName(role.getName());
        if (existRole != null && !role.getId().equals(existRole.getId())) {
            throw new DataException(Return.VALIDATION_ERROR, "更新失败，角色名称已存在");
        }
        this.save(role);
    }
}
