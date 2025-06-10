package io.github.nbb.iot.gateway.store;

import io.github.nbb.iot.common.domain.SerialServerDO;
import io.github.nbb.iot.gateway.framework.netty.NettyConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 胡鹏
 */
@Component
public class SerialServerStore extends BaseStore<SerialServerDO> {

    private final ConcurrentHashMap<String, SerialServerDO> id2Entity = new ConcurrentHashMap<>();

    @Autowired
    private NettyConnectionManager nettyConnectionManager;


    public SerialServerStore() {
        super("serial-server.json", SerialServerDO.class);
    }

    @Override
    void receiveDataList(List<SerialServerDO> dataList) {
        // 重新加载暴露url映射
        id2Entity.clear();
        dataList.forEach(item -> id2Entity.put(item.getId(), item));

        nettyConnectionManager.reload(dataList);
    }


}
