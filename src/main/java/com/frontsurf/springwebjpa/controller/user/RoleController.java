package com.frontsurf.springwebjpa.controller.user;

import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.ListRequestParam;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.common.validate.Common;
import com.frontsurf.springwebjpa.common.validate.Save;
import com.frontsurf.springwebjpa.common.validate.Update;
import com.frontsurf.springwebjpa.domain.user.Role;
import com.frontsurf.springwebjpa.service.user.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/24 1:24
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/list")
    public Return list(ListRequestParam requestParam, Role role) {
        Pager<Role> roles = roleService.listRolePager(requestParam, role);
        return Return.success(roles);
    }

    @PostMapping("/save")
    public Return save(@RequestBody @Validated({Common.class, Save.class}) Role role) {
        try {
            roleService.saveRole(role);
        } catch (DataException e) {
            e.printStackTrace();
            return Return.fail(e.getCode(), e.getErrorMessage());
        }
        return Return.success("角色新增成功");
    }

    @PostMapping("/update")
    public Return update(@RequestBody @Validated({Common.class, Update.class}) Role role) {
        try {
            roleService.updateRole(role);
        } catch (DataException e) {
            e.printStackTrace();
            return Return.fail(e.getCode(), e.getErrorMessage());
        }
        return Return.success("角色更新成功");
    }

    @PostMapping("/delete")
    public Return delete(@RequestParam(name = "ids") List<String> ids) {

        for (String id : ids) {
            Role role = roleService.findById(id);
            if (role != null) {
                roleService.del(role);
            }
        }
        return Return.success("用户删除成功");
    }

}
