package com.frontsurf.springwebjpa.common.config;

import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.constant.DataBaseConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ji.hongtao
 * @Description
 * @date 2017/12/22 13:40
 * @Email ji.hongtao@frontsurf.com
 */
public class QueryConfig {
    public static Map<String, Object> defaultParams = new HashMap<>();
    static {
        defaultParams.put(DataBaseConstant.DELETE_FLAG, CommonConstant.DEL_FLAG_0);
    }
}
