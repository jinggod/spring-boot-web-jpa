package com.frontsurf.springwebjpa;

import com.frontsurf.springwebjpa.RequestType;
import com.frontsurf.springwebjpa.testbase.common.CustomSqlListVerify;
import com.frontsurf.springwebjpa.testbase.common.CustomSqlOneVerify;
import com.frontsurf.springwebjpa.testbase.util.LoginMockUtil;
import com.frontsurf.springwebjpa.testbase.util.TestUtils;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * @Author xu.xiaojing
 * @Date 2018/11/8 23:04
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional  // 事务回滚的注解，默认不回滚，@Rollback(true)已取消
public abstract class BaseTest {

    @Autowired
    public MockMvc mockMvc;

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockHttpServletRequest mockHttpServletRequest;
    private MockHttpServletResponse mockHttpServletResponse;

    private Map<String, String> baseParamMap = new HashMap<>();
    MockHttpSession session;


    @Autowired
    protected JdbcTemplate jdbcTemplate;

//    @Autowired
//    private Filter springSecurityFilterChain;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;


    @Before
//    @WithUserDetails("test001")
    public void init() throws Exception {
        mockHttpServletRequest = new MockHttpServletRequest(webApplicationContext.getServletContext());
        mockHttpServletResponse = new MockHttpServletResponse();
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpServletRequest.setSession(mockHttpSession);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain) // 必须得加这个Filter
                .apply(springSecurity()).
                        build();
        this.login();
    }

    protected void login() throws Exception {
        session = LoginMockUtil.springSecurityLogin(mockMvc);
    }

    protected void login(String mockUserName, String mockPassword) throws Exception {
        session = LoginMockUtil.springSecurityLogin(mockMvc, mockUserName, mockPassword);
    }


    @After
    public void finishTest() {
        System.out.println("----------------------测试完成--------------------");
    }


    /**
     * @param requestType 请求的提交类型
     * @param url         访问的servlet路径
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url) {

        return TestUtils.baseMockMVC(mockMvc, requestType, url, baseParamMap, null, session);
    }

    /**
     * @param requestType 请求的提交类型
     * @param url         访问的servlet路径
     * @param paramsMap   form data 参数
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url, Map<String, String> paramsMap) {

        if (baseParamMap != null) {
            paramsMap.putAll(baseParamMap);
        }
        return TestUtils.baseMockMVC(mockMvc, requestType, url, paramsMap, null, session);
    }

    /**
     * @param requestType 请求的提交类型
     * @param url         访问的servlet路径
     * @param paramsMap   form data参数
     * @param status      http状态码
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url, Map<String, String> paramsMap, Integer status) {

        if (baseParamMap != null) {
            paramsMap.putAll(baseParamMap);
        }
        return TestUtils.baseMockMVC(mockMvc, requestType, url, paramsMap, null, session);
    }


    /**
     * @param requestType
     * @param url
     * @param paramsContent json格式的参数（body）
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url, String paramsContent) {

        return TestUtils.baseMockMVC(mockMvc, requestType, url, baseParamMap, paramsContent, session);
    }

    /**
     * @param requestType
     * @param url
     * @param paramContent json格式的参数（body）
     * @param status       http状态码
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url, String paramContent, Integer status) {

        return TestUtils.baseMockMVC(mockMvc, requestType, url, baseParamMap, paramContent, session);
    }

    /**
     * @param requestType
     * @param url
     * @param paramsMap    form-data格式的参数
     * @param paramContent json格式的参数（body）
     * @param status       http状态码
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url, Map<String, String> paramsMap, String paramContent, Integer status) {

        if (baseParamMap != null) {
            paramsMap.putAll(baseParamMap);
        }
        return TestUtils.baseMockMVC(mockMvc, requestType, url, paramsMap, paramContent, session);
    }

    /**
     * @param requestType
     * @param url
     * @param paramsContent json格式的参数（body）
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url, DataEntity paramsContent) {

        return TestUtils.baseMockMVC(mockMvc, requestType, url, baseParamMap, TestUtils.toJson(paramsContent), session);

    }

    /**
     * @param requestType
     * @param url
     * @param paramContent json格式的参数（body）
     * @param status       http状态码
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url, DataEntity paramContent, Integer status) {

        return TestUtils.baseMockMVC(mockMvc, requestType, url, baseParamMap, TestUtils.toJson(paramContent), session);
    }

    /**
     * @param requestType
     * @param url
     * @param paramsMap    form-data格式的参数
     * @param paramContent json格式的参数（body）
     * @param status       http状态码
     * @return
     */
    protected MvcResult executeBaseRequest(RequestType requestType, String url, Map<String, String> paramsMap, DataEntity paramContent, Integer status) {

        if (baseParamMap != null) {
            paramsMap.putAll(baseParamMap);
        }
        return TestUtils.baseMockMVC(mockMvc, requestType, url, paramsMap, TestUtils.toJson(paramContent), session);
    }

    public void donothing() {

    }


    protected void executeSqls(String... sqls) {

        if (sqls.length <= 0) {
            return;
        }
        for (String sql : sqls) {
            jdbcTemplate.execute(sql);
        }

    }


    protected <T> T executesqlForOne(Class<T> tclass, String sql) {
        return this.executesqlForOne(tclass, sql, null);
    }

    protected <T> T executesqlForOne(Class<T> tclass, String sql, CustomSqlOneVerify<T> customSqlOneVerify) {
        RowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(tclass);
        T t = jdbcTemplate.queryForObject(sql, rowMapper);
        Assert.assertTrue(t != null);
        TestUtils.outpustResult("// sql执行结果：\n\n" + TestUtils.toJson(t));
        if (customSqlOneVerify != null) {
            Assert.assertTrue(customSqlOneVerify.verify(t));
        }
        return t;
    }

    protected <T> List<T> executesqlForList(Class<T> tClass, String sql, CustomSqlListVerify<T> customSqlListVerify) {
        RowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(tClass);
        List<T> list = jdbcTemplate.query(sql, rowMapper);
        Assert.assertTrue(list != null);
        TestUtils.outpustResult("// sql执行结果：\n\n" + TestUtils.toJson(list));
        if (customSqlListVerify != null) {
            Assert.assertTrue(customSqlListVerify.verify(list));
        }
        return list;
    }


}
