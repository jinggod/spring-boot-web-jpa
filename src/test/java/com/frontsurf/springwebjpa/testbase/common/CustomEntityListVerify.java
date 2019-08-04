package com.frontsurf.springwebjpa.testbase.common;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/19 11:28
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public abstract class CustomEntityListVerify<T> implements CustomListVerify{

    Class<T> tClass;

    public CustomEntityListVerify(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public boolean verify(JsonArray array) {
        Gson gson = new Gson();
        Type type = new ParameterizedTypeImpl(new Type[]{tClass}, null, ArrayList.class);
        List<T> tList = gson.fromJson(array, type);
        return this.verifyList(tList);
    }

    /**
     * 返回的接口是json数据的，请重新此方法
     *
     * @param list
     * @return
     */
    public abstract boolean verifyList(List<T> list);
}
