package io.github.nbb.iot.gateway.controller;


import io.github.nbb.iot.common.domain.SerialServerDO;
import io.github.nbb.iot.gateway.framework.netty.NettyConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    private NettyConnectionManager nettyConnectionManager;

    @RequestMapping("/send")
    public String send(String message, String ip, int port) {
        SerialServerDO serialServerDO = new SerialServerDO();
        serialServerDO.setId("1");
        serialServerDO.setHost(ip);
        serialServerDO.setPort(port);

        nettyConnectionManager.sendMessage(serialServerDO, message);
        return "success";
    }
}
