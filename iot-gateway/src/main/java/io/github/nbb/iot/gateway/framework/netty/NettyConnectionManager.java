package io.github.nbb.iot.gateway.framework.netty;

import io.github.nbb.iot.common.domain.SerialServerDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NettyConnectionManager {

    Map<SerialServerDO, ReconnectableNettyClient> connections = new ConcurrentHashMap<>();

    public synchronized void reload(List<SerialServerDO> serialServerList) {
        // 需要关闭的Client
        this.connections.entrySet().stream()
                .filter(item -> !serialServerList.contains(item.getKey()))
                .map(Map.Entry::getValue)
                .forEach(item -> item.shutdown());

        // 构建新的Connection
        Map<SerialServerDO, ReconnectableNettyClient> newConnections = serialServerList.stream()
                .collect(Collectors.toMap(item -> item, item -> this.connections.getOrDefault(item, new ReconnectableNettyClient(item))));
        this.connections = newConnections;
    }


    public void sendMessage(SerialServerDO serialServerInfo, String message) {
        connections.get(serialServerInfo).sendMessage(message);
    }
}
