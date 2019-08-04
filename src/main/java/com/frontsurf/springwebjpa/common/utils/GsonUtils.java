package com.frontsurf.springwebjpa.common.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/20 17:16
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class GsonUtils {

    /**
     * 提供泛型对象的
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> List<T> parseString2List(String json,Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list =  new Gson().fromJson(json, type);
        return list;
    }

    /**
     * Gson 解析列表的DEMO
     * @param args
     */
    public static void main(String[] args) {

        String jsonString = "[{\"name\":\"小花\",\"score\":\"50\"}]";
        //先转换成数组
        Student[] array = new Gson().fromJson(jsonString,Student[].class);
        List<Student> list = Arrays.asList(array);
        System.out.println(Arrays.toString(array));

        // 使用 TypeToken
        Type type = new TypeToken<List<Student>>(){}.getType();
        List<Student> list2 = new Gson().fromJson(jsonString,type);
        System.out.println(Arrays.toString(list2.toArray()));
    }

    public static class Student{
        String name;
        int score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
