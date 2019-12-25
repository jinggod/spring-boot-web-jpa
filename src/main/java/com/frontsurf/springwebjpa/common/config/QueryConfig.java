package com.frontsurf.springwebjpa.common.config;

import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.constant.DataBaseConstant;
import com.frontsurf.springwebjpa.common.utils.jpa.FieldQueryParam;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryParam;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author ji.hongtao
 * @Description
 * @date 2017/12/22 13:40
 * @Email ji.hongtao@frontsurf.com
 */
public class QueryConfig {
    public static List<QueryParam> defaultParams = new LinkedList<>();
    static {
        defaultParams.add(new FieldQueryParam(DataBaseConstant.DELETE_FLAG, CommonConstant.DEL_FLAG_0));
    }
}
