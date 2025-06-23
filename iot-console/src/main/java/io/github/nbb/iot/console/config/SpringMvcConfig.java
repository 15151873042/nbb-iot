package io.github.nbb.iot.console.config;

import io.github.nbb.iot.console.framework.web.converter.StringToDateConverter;
import io.github.nbb.iot.console.framework.web.converter.StringToLocalDateConverter;
import io.github.nbb.iot.console.framework.web.converter.StringToLocalDateTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 胡鹏
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    /**
     * 添加数据类型转换器，用于x-www-form-urlencoded传参
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
        registry.addConverter(new StringToLocalDateConverter());
        registry.addConverter(new StringToLocalDateTimeConverter());
    }
}
