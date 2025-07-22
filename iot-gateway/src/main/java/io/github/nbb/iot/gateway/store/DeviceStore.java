package io.github.nbb.iot.gateway.store;

import io.github.nbb.iot.common.constants.NacosConfigConstants;
import io.github.nbb.iot.common.domain.IotDeviceDO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 胡鹏
 */
@Component
public class DeviceStore extends BaseStore<IotDeviceDO> {

    /**
     * key：     产品id
     * value：   产品下的所有设备
     */
    private final ConcurrentHashMap<Long, List<IotDeviceDO>> PRODUCT_ID_2_DEVICE = new ConcurrentHashMap<>();

    public DeviceStore() {
        super(NacosConfigConstants.IOT_DEVICE_DATA_ID, IotDeviceDO.class);
    }

    @Override
    void receiveDataList(List<IotDeviceDO> deviceList) {
        Map<Long, List<IotDeviceDO>> productId2Device = deviceList.stream().collect(Collectors.groupingBy(IotDeviceDO::getProductId));

        synchronized (this) {
            PRODUCT_ID_2_DEVICE.clear();
            PRODUCT_ID_2_DEVICE.putAll(productId2Device);
        }
    }

    public List<IotDeviceDO> listByProductId(Long productId) {
        return PRODUCT_ID_2_DEVICE.get(productId);
    }
}
