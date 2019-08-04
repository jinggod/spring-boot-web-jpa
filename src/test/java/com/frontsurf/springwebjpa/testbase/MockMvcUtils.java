package com.frontsurf.springwebjpa.testbase;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Transactional  // 事务回滚的注解，默认不回滚，@Rollback(true)已取消
public class MockMvcUtils {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected FilterChainProxy springSecurityFilterChain;

    protected MockHttpSession mockHttpSession;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain) // 必须得加这个Filter，但是我测试可加可不加
                .apply(springSecurity()).
                        build();
    }

    public MockHttpSession getSession(String username, String password)  {
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(formLogin("/login").user(username).password(password)).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  (MockHttpSession)mvcResult.getRequest().getSession();
    }


    public boolean isSuccess(MvcResult mvcResult){
        return mvcResult.getResponse().getStatus() == 200;
    }

    public boolean vailResultJson(MvcResult mvcResult){
        try {
            String contentAsString = mvcResult.getResponse().getContentAsString();
            if (StringUtils.isBlank(contentAsString)) {
                return false;
            }
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(contentAsString);
            return jsonElement.isJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean contains(MvcResult mvcResult, String text){
        try {
            String contentAsString = mvcResult.getResponse().getContentAsString();
            if (contentAsString.contains(text)){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



//    public MvcResult get(String url, MultiValueMap<String,String> params) throws Exception{
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).params(params).session(mockHttpSession)
//                .contextPath("/mes"))
//                .andReturn();
//        return mvcResult;
//    }



}
