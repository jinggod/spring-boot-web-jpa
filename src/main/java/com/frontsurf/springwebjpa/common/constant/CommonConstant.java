package com.frontsurf.springwebjpa.common.constant;


/**
 * @Author xu.xiaojing
 * @Date 2019/6/19 14:55
 * @Email xu.xiaojing@frontsurf.com
 * @Description 上下文的常量
 * 建议将一些状态常量、类型常量在此处进行管理
 */
public interface CommonConstant {

    /**
     * 禁用状态：0-正常状态，1-禁用
     */
    int STATUS_NORMAL = 0;
    int STATUS_DISABLE = -1;

    /**
     * 删除标志位：0-未删除，1-删除
     */
    int DEL_FLAG_1 = 1;
    int DEL_FLAG_0 = 0;

    /**
     * 参数类型：布尔类型、整型、浮点数、字符串、有符号整型、有符号浮点数
     */
    String VALUE_BOOLEAN = "Boolean";
    String VALUE_INTEGER = "Integer";
    String VALUE_DOUBLE = "Double";
    String VALUE_STRING = "String";
    String VALUE_SIGNED_INTEGER="Signed_Integer";
    String VALUE_SIGNED_DOUBLE="Signed_Double";

    /**
     *
     */


}
