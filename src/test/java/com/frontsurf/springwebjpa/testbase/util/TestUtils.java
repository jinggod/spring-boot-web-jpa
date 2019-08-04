package com.frontsurf.springwebjpa.testbase.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontsurf.springwebjpa.BaseTest;
import com.frontsurf.springwebjpa.RequestType;
import com.frontsurf.springwebjpa.testbase.common.ApplicationContextInTest;
import com.frontsurf.springwebjpa.testbase.common.CustomListVerify;
import com.frontsurf.springwebjpa.testbase.common.CustomVerify;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2018/11/9 10:42
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class TestUtils {

    private static JsonParser parser = new JsonParser();

    /**
     * 判断返回结果的 业务处理是否正确
     * 默认的处理条件
     *
     * @param result
     * @return
     */
    public static boolean isSuccess(MvcResult result) {
        return isMatchCode(result, 200);
    }

    /**
     * 判断返回结果的 业务处理是否正确
     * 默认的处理条件
     *
     * @param result
     * @return
     */
    public static void isSuccessAlert(MvcResult result) {
        isMatchCodeAlert(result, 200, null);
    }


    /**
     * 判断返回结果的 业务处理是否正确
     * 默认的处理条件
     *
     * @param result
     * @return
     */
    public static void isSuccessAlert(MvcResult result, CustomVerify customVerify) {
        isMatchCodeAlert(result, 200, customVerify);
    }

    /**
     * 用于判断列表的查询结果的数量 是否与预估的数量一致
     *
     * @param result
     * @param num
     * @return
     */
    public static void isSuccessList(MvcResult result, int num) {
        isSuccessList(result, num, null);
    }

    /**
     * 用于判断列表的查询结果的数量 是否与预估的数量一致
     *
     * @param result
     * @param num
     * @return
     */
    public static void isSuccessList(MvcResult result, int num, CustomListVerify customListVerify) {


        String content = null;
        try {
            content = result.getResponse().getContentAsString();
            JsonObject jsonObject = parser.parse(content).getAsJsonObject();
            JsonArray arr = null;
            if (jsonObject.get("code").getAsInt() == 200) {
                JsonElement dataElement = jsonObject.get("data");
                if (dataElement.isJsonArray()) {
                    arr = dataElement.getAsJsonArray();
                    Assert.assertTrue(dataElement.getAsJsonArray().size() == num);
                } else {
                    JsonObject dataObj = dataElement.getAsJsonObject();
                    if (dataObj.get("list") != null) {

                        arr = dataObj.get("list").getAsJsonArray();
                        System.out.println("\n----------------查询结果条数：" + arr.size());
                        Assert.assertTrue(arr.size() == num);
                    } else if (dataObj.get("total") != null) {
                        Assert.assertTrue(dataObj.get("total").getAsInt() == num);
                    }
                }
                if (customListVerify != null) {
                    Assert.assertTrue(customListVerify.verify(arr));
                }
            } else {
                System.out.println("\n请求处理失败---------------提示：" + jsonObject.get("message").getAsString());
                Assert.assertTrue(false);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            System.out.println("\n---------------返回结果：");
            System.out.println(JsonFormatUtil.format(content));
            outpustResult(content);
        }
    }

    public static void outpustResult(String content) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = null;
        //从index为2开始，0是java.lang.Thread.getStackTrace,1是com.frontsurf.mes.base.util.TestUtils.outpustResult(TestUtils.java:129)
        for (int i = 2; i < stackTraceElements.length; i++) {
            stackTraceElement = stackTraceElements[i];
            if (!stackTraceElement.getClassName().equals(TestUtils.class.getName()) && !stackTraceElement.getClassName().equals(BaseTest.class.getName())) {
                break;
            }
        }

        String methodName = stackTraceElement.getMethodName();
        String className = stackTraceElement.getClassName();
        className = className.replace("com.frontsurf.mes.", "");
        className = className.replace(".", File.separator);

        String outputDirPath = System.getProperty("user.dir") + File.separator + "out" + File.separator + className;
        String outputFilePath = outputDirPath + File.separator + methodName + ".json";
        File outputDir = new File(outputDirPath);
        File outputFile = new File(outputFilePath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String formateJson = JsonFormatUtil.format(content);
        try {
            FileWriter writer;
            if (!methodName.equals(ApplicationContextInTest.PRE_METHOD.get())) {
                writer = new FileWriter(outputFile);
                writer.write(formateJson);
                ApplicationContextInTest.PRE_METHOD.set(methodName);
            } else {
                writer = new FileWriter(outputFile, true);
                writer.append("\n\n\n//\t\t=============================================================================================================================================================================\n\n\n");
                writer.append(formateJson);
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param result
     * @return
     */
    public static boolean isCommonFail(MvcResult result) {
        return isMatchCode(result, Return.COMMON_ERROR);
    }

    /**
     * 判断返回结果的 业务处理是否正确
     * 默认的处理条件
     *
     * @param result
     * @return
     */
    public static boolean isMatchCode(MvcResult result, Integer code) {
        String content = null;
        try {
            content = result.getResponse().getContentAsString();

            JsonObject jsonObject = parser.parse(content).getAsJsonObject();
            if (jsonObject.get("code").getAsInt() == code) {
                System.out.println("\n请求处理成功---------------结果：" + content);
                return true;
            } else {
                System.out.println("\n请求处理失败---------------提示：" + jsonObject.get("message").getAsString());
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            outpustResult(content);
        }
        return false;
    }

    /**
     * 判断返回结果的 业务处理是否正确
     *
     * @param result
     * @return
     */
    public static void isMatchCodeAlert(MvcResult result, Integer code) {

        isMatchCodeAlert(result, code, null);
    }


    /**
     * 判断返回结果的 业务处理是否正确
     *
     * @param result
     * @return
     */
    public static void isMatchCodeAlert(MvcResult result, Integer code, CustomVerify customVerify) {
        String content = null;
        try {
            content = result.getResponse().getContentAsString();

            JsonObject jsonObject = parser.parse(content).getAsJsonObject();
            Assert.assertTrue(jsonObject.get("code").getAsInt() == code);
            if (customVerify != null) {
                Assert.assertTrue(customVerify.verify(jsonObject.get("data").getAsJsonObject()));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        } finally {
            System.out.println("\n-------------------请求结果：");
            System.out.println(JsonFormatUtil.format(content));
            outpustResult(content);
        }
//        return false;
    }


    /**
     * 转换成json
     *
     * @param param
     * @return
     */
    public static String toJson(Object param) {
        try {
            return buildObjectMapper().writeValueAsString(param);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static boolean isSuccess(MvcResult result, String data) {
        try {
            String content = result.getResponse().getContentAsString();

            JsonObject jsonObject = parser.parse(content).getAsJsonObject();
            if (jsonObject.get("code").getAsInt() == 200) {
                System.out.println("\n请求处理成功---------------结果：" + content);
                if (data.equals(jsonObject.get("data").getAsString())) {
                    return true;
                }
                return false;
            } else {
                System.out.println("\n请求处理失败---------------提示：" + jsonObject.get("message").getAsString());
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 处理基本的GET、POST请求，并匹配请求结果的HTTP状态码为200
     *
     * @param mockMvc
     * @param requestType
     * @param url
     * @return
     */
    public static MvcResult baseMockMVC(MockMvc mockMvc, RequestType requestType, String url, Map<String, String> paramsMap, String paramContent, MockHttpSession session) {

        MvcResult result = null;
        result = TestUtils.baseMockMVC(mockMvc, requestType, url, paramsMap, paramContent, 200, session);
        return result;
    }

    /**
     * 处理基本的GET、POST请求，并匹配请求结果的HTTP状态码为指定的状态码
     *
     * @param mockMvc
     * @param requestType
     * @param url
     * @param httpStatus  http状态码
     * @return
     */
    public static MvcResult baseMockMVC(MockMvc mockMvc, RequestType requestType, String url, Map<String, String> paramsMap, String paramContent, Integer httpStatus, MockHttpSession session) {

        MvcResult result = null;
        MockHttpServletRequestBuilder builder = TestUtils.createBuilder(requestType, url, paramsMap, paramContent, session);

        try {
            result = mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(httpStatus)).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static MockHttpServletRequestBuilder createBuilder(RequestType requestType, String url, Map<String, String> paramsMap, String paramContent, MockHttpSession session) {
        MockHttpServletRequestBuilder builder = null;
        switch (requestType) {
            case GET:
                if (session != null) {
                    builder = MockMvcRequestBuilders.get(url).session(session);
                } else {
                    builder = MockMvcRequestBuilders.get(url);
                }
                break;
            case POST:
                if (session != null) {
                    builder = MockMvcRequestBuilders.post(url).session(session);
                } else {
                    builder = MockMvcRequestBuilders.post(url);
                }
                break;
            default:
                break;
        }

        if (paramsMap != null) {
            for (Map.Entry<String, String> item : paramsMap.entrySet()) {
                builder.contentType(MediaType.APPLICATION_FORM_URLENCODED);
                builder.param(item.getKey(), item.getValue());
            }
        }

        if (paramContent != null) {
            builder.content(paramContent);
            builder.contentType(MediaType.APPLICATION_JSON_UTF8);
        }

        return builder;
    }

    private static ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        objectMapper.getSerializationConfig().with(dateFormat);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }


}
