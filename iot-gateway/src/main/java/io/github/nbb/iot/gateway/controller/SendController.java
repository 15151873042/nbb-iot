package io.github.nbb.iot.gateway.controller;


import io.github.nbb.iot.gateway.framework.boot.GatewayApplicationRunner;
import io.github.nbb.iot.gateway.properties.SerialServerProperties;
import io.github.nbb.iot.gateway.properties.SerialServerProperties.SerialServerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    private GatewayApplicationRunner runner;

    @RequestMapping("/send")
    public String send(String message, String ip, int port) {
        runner.sendMessage(new SerialServerInfo(ip, port), message);
        return "success";
    }
}
