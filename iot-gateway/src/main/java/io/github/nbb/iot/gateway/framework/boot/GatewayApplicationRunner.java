package io.github.nbb.iot.gateway.framework.boot;

import io.github.nbb.iot.gateway.framework.netty.ReconnectableNettyClient;
import io.github.nbb.iot.gateway.properties.SerialServerProperties;
import io.github.nbb.iot.gateway.properties.SerialServerProperties.SerialServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class GatewayApplicationRunner implements ApplicationRunner {

    @Autowired
    SerialServerProperties serverProperties;

    private ConcurrentHashMap<SerialServerInfo, ReconnectableNettyClient> info2Client = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) {
        for (SerialServerInfo serialServerInfo : serverProperties.getServerList()) {
            ReconnectableNettyClient nettyClient = new ReconnectableNettyClient(serialServerInfo);
            nettyClient.init();
            info2Client.put(serialServerInfo, nettyClient);
        }
    }

    public void sendMessage(SerialServerInfo serialServerInfo, String message) {
        info2Client.get(serialServerInfo).sendMessage(message);
    }
}
