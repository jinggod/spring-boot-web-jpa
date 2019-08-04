package com.frontsurf.springwebjpa.testbase.util;

import com.frontsurf.springwebjpa.RequestType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/15 17:13
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class LoginMockUtil {

    private static String mockUserName = "admin";
    private static String mockPassword = "123456";

    /**
     * 普通登陆
     *
     * @param mockMvc
     * @return
     */
    public static MockHttpSession CommonLogin(MockMvc mockMvc) {
        //模拟远程返回结果
        MockHttpSession mockHttpSession = null;
        //登陆
        MvcResult result = TestUtils.baseMockMVC(mockMvc, RequestType.POST, "/login?username=admin&password=123456", null, null, null);
        TestUtils.isSuccessAlert(result);
        mockHttpSession = (MockHttpSession) result.getRequest().getSession();

//        MesUser mesUser = (MesUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        mesUser.getMesId();
        return mockHttpSession;
    }

    /**
     * Spring security框架的登陆方式
     *
     * @param mockMvc
     * @return
     */
    public static MockHttpSession springSecurityLogin(MockMvc mockMvc) throws Exception {
        //模拟远程返回结果
        MockHttpSession mockHttpSession = null;
        MvcResult result = mockMvc.perform(formLogin("/login?encrypt=0").user(mockUserName).password(mockPassword)).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
//        TestUtils.isSuccessAlert(result);
        mockHttpSession = (MockHttpSession) result.getRequest().getSession();

        return mockHttpSession;
    }

    public static MockHttpSession springSecurityLogin(MockMvc mockMvc, String mockUserName, String mockPassword) throws Exception {
        //模拟远程返回结果
        MockHttpSession mockHttpSession = null;
        MvcResult result = mockMvc.perform(formLogin("/login").user(mockUserName).password(mockPassword)).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
//        TestUtils.isSuccessAlert(result);
        mockHttpSession = (MockHttpSession) result.getRequest().getSession();

        return mockHttpSession;
    }

    public static String getMockUserName() {
        return mockUserName;
    }

    public static String getMockPassword() {
        return mockPassword;
    }

    public static String getEncoderPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(mockPassword);
    }


}
