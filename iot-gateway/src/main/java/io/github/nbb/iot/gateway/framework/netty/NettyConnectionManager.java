package io.github.nbb.iot.gateway.framework.netty;

import io.github.nbb.iot.gateway.properties.SerialServerProperties;
import io.github.nbb.iot.gateway.properties.SerialServerProperties.SerialServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class NettyConnectionManager {

    Map<SerialServerInfo, ReconnectableNettyClient> connections = new ConcurrentHashMap<>();


    final SerialServerProperties serverProperties;

    public NettyConnectionManager(SerialServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @PostConstruct
    public void init() {
        for (SerialServerInfo serialServerInfo : serverProperties.getServerList()) {
            ReconnectableNettyClient nettyClient = new ReconnectableNettyClient(serialServerInfo);
            connections.put(serialServerInfo, nettyClient);
        }
    }

    public void sendMessage(SerialServerInfo serialServerInfo, String message) {
        connections.get(serialServerInfo).sendMessage(message);
    }
}
