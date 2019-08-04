package com.frontsurf.springwebjpa.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/25 11:17
 * @Email xu.xiaojing@frontsurf.com
 * @Description 配置JSON的序列化模块，解决Hibernate的是实体类在序列化时，会触发所有的懒加载。此配置可以在序列化时，只序列化已经有值的字段，不触发懒加载
 */
@Configuration
public class HibernateConfiguration {

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        //不能是全新的ObjectMapper，否则将会在配置文件配置的json配置将会失效，特别是时区问题
//        ObjectMapper objectMapper = jsonConverter.getObjectMapper();
        jsonConverter.setObjectMapper(objectMapper);
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(hibernate5Module);
        return jsonConverter;
    }
}
