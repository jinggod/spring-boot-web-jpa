package com.frontsurf.springwebjpa.testbase.common;

import com.google.gson.JsonArray;

/**
 * @Author xu.xiaojing
 * @Date 2019/4/19 11:41
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public abstract class CustomJsonListVerify implements  CustomListVerify{

    @Override
    public boolean verify(JsonArray arr) {
        return this.verifyList(arr);
    }

    public abstract boolean verifyList(JsonArray arr);
}
