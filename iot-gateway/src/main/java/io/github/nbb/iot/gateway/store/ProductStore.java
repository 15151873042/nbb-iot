package io.github.nbb.iot.gateway.store;

import io.github.nbb.iot.common.constants.NacosConfigConstants;
import io.github.nbb.iot.common.domain.IotDeviceDO;
import io.github.nbb.iot.common.domain.IotProductDO;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author 胡鹏
 */
@Component
public class ProductStore extends BaseStore<IotProductDO> implements SmartInitializingSingleton {

    @Autowired
    private Scheduler scheduler;

    private List<IotProductDO> PRODUCT_LIST = new CopyOnWriteArrayList<>();

    public ProductStore() {
        super(NacosConfigConstants.IOT_PRODUCT_DATA_ID, IotProductDO.class);
    }

    @Override
    void receiveDataList(List<IotProductDO> deviceList) {
        synchronized (this) {
            PRODUCT_LIST.clear();
            PRODUCT_LIST.addAll(deviceList);
        }
    }


    @Override
    public void afterSingletonsInstantiated() {
        System.out.println(PRODUCT_LIST);
    }
}
