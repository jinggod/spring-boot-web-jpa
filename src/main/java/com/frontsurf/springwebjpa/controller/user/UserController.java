package com.frontsurf.springwebjpa.controller.user;

import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.jpa.JPAUtils;
import com.frontsurf.springwebjpa.common.utils.web.ListRequestParam;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.common.utils.web.UserInfo;
import com.frontsurf.springwebjpa.common.validate.Common;
import com.frontsurf.springwebjpa.common.validate.Save;
import com.frontsurf.springwebjpa.common.validate.Update;
import com.frontsurf.springwebjpa.domain.user.User;
import com.frontsurf.springwebjpa.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/24 0:22
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/list")
    public Return list(ListRequestParam listRequestParam, User user) {

        Pager<User> userPager = userService.listUserPager(listRequestParam, user);
        return Return.success(userPager);
    }

    @GetMapping("getById")
    public Return getUserById(String id) {
        User user = userService.findByIdUseSql(id);
        return Return.success(user);
    }

    @PostMapping("/save")
    public Return save(@RequestBody @Validated({Common.class, Save.class}) User user) {
        try {
            userService.saveUser(user);
        } catch (DataException e) {
            e.printStackTrace();
            return Return.fail(e.getCode(), e.getErrorMessage());
        }
        return Return.success("新增成功");
    }

    @PostMapping("/update")
    public Return update(@RequestBody @Validated({Common.class, Update.class}) User user) {

        try {
            userService.updateUser(user);
        } catch (DataException e) {
            e.printStackTrace();
            return Return.fail(e.getCode(), e.getErrorMessage());
        }

        return Return.success("新增成功");
    }

    @PostMapping("/delete")
    public Return delete(@RequestParam(name = "ids") List<String> ids) {

        for (String id : ids) {
            User user = userService.findById(id);
            if (user != null) {
                userService.del(user);
            }
        }
        return Return.success("用户删除成功");
    }

    @GetMapping("/myinfo")
    public Return getCurrentUserInfo() throws DataException {
        String userId = UserInfo.getUserId();
        User currUser = userService.findById(userId);
        JPAUtils.lazyLoad(currUser, "roles");
        return Return.success(currUser);
    }

}
