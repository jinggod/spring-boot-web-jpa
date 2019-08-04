package com.frontsurf.springwebjpa.common.utils.web;

import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserInfo {

    public static String getUserName() throws DataException {
        String name;
        try {
            User mesUser = getCurrentUser();
            name = mesUser.getUsername();
        } catch (Exception e) {
            name = null;
        }
        if (name == null) {
            throw new DataException(400, "用户未登录，获取不了用户名");
        }
        return name;
    }

    public static String getUserId() throws DataException {
        String id;
        try {
            User user = getCurrentUser();
            id = user.getId();
        } catch (Exception e) {
            id = null;
        }
        if (id == null) {
            throw new DataException(Return.COMMON_ERROR, "用户尚未登录，获取不了用户ID");
        }
        return id;
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
