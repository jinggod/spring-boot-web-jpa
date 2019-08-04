package com.frontsurf.springwebjpa.controller.user;

import com.frontsurf.springwebjpa.BaseTest;
import com.frontsurf.springwebjpa.RequestType;
import com.frontsurf.springwebjpa.testbase.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/8/4 19:02
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class UserControllerTest extends BaseTest {

    private String listUrl = "/user/list";
    private String deleteUrl = "/user/delete";
    private String saveUrl = "/user/save";
    private String updateUrl = "/user/";

    @Before
    public void dbInit() {
        super.executeSqls(
                "DELETE FROM user"
                ,"INSERT INTO `test`.`user`(`id`, `username`, `password`, `create_by`, `create_name`, `create_date`, `update_by`, `update_date`, `del_flag`) VALUES ('1', 'admin', '$2a$10$c/2kE5KG.kRwx9qTkHl9SuGDiM/s3SO4ouAZaM1K4ryz6hDImf.tu', NULL, NULL, NULL, NULL, NULL, 0)"
                ,"INSERT INTO `test`.`user`(`id`, `username`, `password`, `create_by`, `create_name`, `create_date`, `update_by`, `update_date`, `del_flag`) VALUES ('4028e3996b8d461f016b8d4711330001', 'aaa', '$2a$10$c/2kE5KG.kRwx9qTkHl9SuGDiM/s3SO4ouAZaM1K4ryz6hDImf.tu', '1', NULL, '2019-06-25 14:16:13', NULL, NULL, 0)"
                ,"INSERT INTO `test`.`user`(`id`, `username`, `password`, `create_by`, `create_name`, `create_date`, `update_by`, `update_date`, `del_flag`) VALUES ('8c03a00b-ee63-46bc-b2be-870c9c66ca5d', 'ming', '$2a$10$IOxQA.Q6qcTZbetEaT40feQ8TSemAH2v./d7XfXQFPNNMHzbgy0pi', '1', 'admin', '2019-08-04 17:08:00', '1', '2019-08-04 17:08:00', 0)"
                //,""
        );
    }

    // ==================================================== LIST ==========================================

    /**
     * 正常情况1：无参
     */
    @Test
    public void list() {
        Map<String, String> params = new HashMap<>();
        MvcResult result = super.executeBaseRequest(RequestType.GET, listUrl, params);
        TestUtils.isSuccessList(result, 3);
    }

    //========================================= SAVE ==================================================

    @Test
    public void save() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getCurrentUserInfo() {
    }
}