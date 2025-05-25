package io.github.nbb.iot.gateway.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "serial")
public class SerialServerProperties {

    private List<SerialServerInfo> serverList;


    @Data
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SerialServerInfo {

        private String ip;

        private int port;
    }
}
