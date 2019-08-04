package com.frontsurf.springwebjpa.common.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/30 18:28
 * @Email xu.xiaojing@frontsurf.com
 * @Description controller层接收时间格式（只能是 LocalDate、LocalTime、LocalDateTime），
 *              配置此类后，可以直接在controller处，根据业务需求的时间格式，来使用相应的类；
 */

@Configuration
public class JsonToLocalDateConfiguration {

    @Bean
    public Converter<String, String> StringConvert() {
        return new Converter<String, String>() {
            @Override
            public String convert(String source) {
                return StringUtils.trimToNull(source);
            }
        };
    }

    @Bean
    public Converter<String, LocalTime> LocalTimeConvert() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }
                return LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm:ss"));
            }

        };
    }

    @Bean
    public Converter<String, LocalDate> LocalDateConvert() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }
                return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

        };
    }

    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConvert() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

        };
    }


}
