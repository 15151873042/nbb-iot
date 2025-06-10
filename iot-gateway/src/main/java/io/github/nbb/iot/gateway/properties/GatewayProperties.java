package io.github.nbb.iot.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 胡鹏
 */
@Data
@Component
@ConfigurationProperties(prefix = "iot-gateway")
public class GatewayProperties {

    /** 当前iot-gateway对应的clientId */
    private String clientId;
}
