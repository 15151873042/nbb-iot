package io.github.nbb.iot.gateway.controller;


import io.github.nbb.iot.common.domain.IotSerialDO;
import io.github.nbb.iot.gateway.framework.netty.NettyConnectionManager;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    private NettyConnectionManager nettyConnectionManager;

    @Autowired
    private Scheduler scheduler;

    @RequestMapping("/send")
    public String send(String message, String ip, int port) {
        IotSerialDO serialServerDO = new IotSerialDO();
        serialServerDO.setId(1L);
        serialServerDO.setIp(ip);
        serialServerDO.setPort(port);

        nettyConnectionManager.sendMessage(serialServerDO, message);
        return "success";
    }
}
