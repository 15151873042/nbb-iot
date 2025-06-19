package io.github.nbb.iot.console.config;

import io.github.nbb.iot.console.framework.jackson.BigDecimalSerializer;
import io.github.nbb.iot.console.framework.jackson.NumberSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class JacksonConfig {

    /**
     * ObjectMapper已经在JacksonAutoConfiguration自动状态，此处只需要自定义Jackson2ObjectMapperBuilder即可
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeObjectMapper() {
        return jacksonObjectMapperBuilder -> {
            // Long类型转换成String，已避免（将超过16位的long，前端精度丢失）
            jacksonObjectMapperBuilder.serializerByType(Long.class, NumberSerializer.INSTANCE);
            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, NumberSerializer.INSTANCE);
            jacksonObjectMapperBuilder.serializerByType(BigDecimal.class, BigDecimalSerializer.INSTANCE);
        };
    }
}
