package io.github.nbb.iot.gateway.store;

import io.github.nbb.iot.common.constants.NacosConfigConstants;
import io.github.nbb.iot.common.domain.IotDeviceDO;
import io.github.nbb.iot.common.domain.IotProductDO;
import io.github.nbb.iot.gateway.task.ProductTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 胡鹏
 */
@Component
public class ProductStore extends BaseStore<IotProductDO> {

    @Autowired
    private ProductTask productTask;

    /**
     * key：     产品id
     * value：   产品信息
     */
    private final ConcurrentHashMap<Long, IotProductDO> PRODUCT_ID_2_PRODUCT = new ConcurrentHashMap<>();

    public ProductStore() {
        super(NacosConfigConstants.IOT_PRODUCT_DATA_ID, IotProductDO.class);
    }

    @Override
    void receiveDataList(List<IotProductDO> productList) {
        synchronized (this) {
            productList.forEach(product -> PRODUCT_ID_2_PRODUCT.put(product.getId(), product));
            productTask.reloadTask(productList);
        }
    }

    public IotProductDO getById(Long productId) {
        return PRODUCT_ID_2_PRODUCT.get(productId);
    }
}
