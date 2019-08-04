package com.frontsurf.springwebjpa.testbase.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/18 15:50
 * @Email xu.xiaojing@frontsurf.com
 * @Description 使用返回的原始数据进行自定义校验内容
 */

public abstract class CustomJsonVerify implements CustomVerify {

    @Override
    public boolean verify(JsonElement element) {
        return this.verifyObj(element.getAsJsonObject());
    }

    /**
     * 返回的结果是个json对象的，请重写此方法
     *
     * @param jsonObject
     * @return
     */
    public abstract boolean verifyObj(JsonObject jsonObject);


}
