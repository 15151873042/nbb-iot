package io.github.nbb.iot.console.controller;

import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.common.notify.NotifyCenter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author 胡鹏
 */
@RestController
public class TestController implements SmartInitializingSingleton {

    @Autowired
    private NacosServiceManager nacosServiceManager;

    private List<String> onLineClientIds = new CopyOnWriteArrayList<>();



    @GetMapping("/isOnline")
    public String inOnline(String clientId) {
        return onLineClientIds.contains(clientId) ? "在线" : "离线";
    }


    @SneakyThrows
    @Override
    public void afterSingletonsInstantiated() {
        NamingService namingService = nacosServiceManager.getNamingService();
        namingService.subscribe("iot-gateway", "DEFAULT_GROUP", event -> {
            if (event instanceof NamingEvent) {
                NamingEvent namingEvent = (NamingEvent) event;

                List<String> onLineClientIds = namingEvent.getInstances().stream()
                        .map(instance -> instance.getMetadata().get("client-id")).collect(Collectors.toList());

                this.onLineClientIds.clear();
                this.onLineClientIds.addAll(onLineClientIds);
            }
        });
    }
}
