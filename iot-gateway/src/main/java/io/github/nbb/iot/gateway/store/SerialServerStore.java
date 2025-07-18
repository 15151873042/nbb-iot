package io.github.nbb.iot.gateway.store;

import io.github.nbb.iot.common.domain.IotSerialDO;
import io.github.nbb.iot.gateway.framework.netty.NettyConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.nbb.iot.common.constants.NacosConfigConstants.IOT_SERIAL_DATA_ID;

/**
 * @author 胡鹏
 */
@Component
public class SerialServerStore extends BaseStore<IotSerialDO> {

    private final ConcurrentHashMap<Long, IotSerialDO> id2Entity = new ConcurrentHashMap<>();

    @Autowired
    private NettyConnectionManager nettyConnectionManager;


    public SerialServerStore() {
        super(IOT_SERIAL_DATA_ID, IotSerialDO.class);
    }

    @Override
    void receiveDataList(List<IotSerialDO> dataList) {
        // 重新加载暴露url映射
        id2Entity.clear();
        dataList.forEach(item -> id2Entity.put(item.getId(), item));

        nettyConnectionManager.reload(dataList);
    }
}
