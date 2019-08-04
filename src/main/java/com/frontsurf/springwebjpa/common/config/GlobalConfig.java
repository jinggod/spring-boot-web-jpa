package com.frontsurf.springwebjpa.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/19 14:58
 * @Email xu.xiaojing@frontsurf.com
 * @Description 全局配置，多处使用到的配置应放此处
 */

@Configuration
public class GlobalConfig {

    @Value("${security.rsa.publickey}")
    public String publicKey;

    @Value("${security.rsa.privatekey}")
    public String privateKey;




    public static String FILE_SAVE_PATH;
    //注入到静态属性中
    @Value("${file.save.path}")
    public static void setFileSavePath(String fileSavePath) {
        FILE_SAVE_PATH = fileSavePath;
    }

    public static final Map<String,Object> systemConfigMap = new HashMap<>();
}
