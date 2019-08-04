package com.frontsurf.springwebjpa.testbase.common;


import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/18 15:50
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public abstract class CustomEntityVerify<T> implements CustomVerify {

    Class<T> tClass;


    public CustomEntityVerify(Class<T> tClass) {
        this.tClass = tClass;
    }


    @Override
    public boolean verify(JsonElement element) {
        Gson gson = new Gson();
        T t = gson.fromJson(element, tClass);
        return this.verifyObj(t);
    }

    /**
     * 返回结果是一个对象的，请重写此方法
     *
     * @param t
     * @return
     */
    public abstract boolean verifyObj(T t);


}
