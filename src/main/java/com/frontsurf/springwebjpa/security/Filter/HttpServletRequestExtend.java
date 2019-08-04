package com.frontsurf.springwebjpa.security.Filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/21 11:25
 * @Email xu.xiaojing@frontsurf.com
 * @Description 扩展HttpServletRequest：可修改请求参数的 HttpServletRequest
 */

public class HttpServletRequestExtend extends HttpServletRequestWrapper {

    private Map<String, String[]> params = new HashMap<String, String[]>();

    public HttpServletRequestExtend(HttpServletRequest request) {
        super(request);
        //将参数表，赋予给当前的Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {//重写getParameter，代表参数从当前类中的map获取
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            //转发时，在路径后面的参数是不会设置到扩展类里面的，所以要从父类获取
           return super.getParameter(name);
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {//同上

        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return super.getParameterValues(name);
        }
        return values;
    }

    public void addAllParameters(Map<String, Object> otherParams) {//增加多个参数
        for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }


    public void addParameter(String name, Object value) {//增加参数
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }



}
