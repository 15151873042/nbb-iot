package io.github.nbb.iot.gateway.store;

import io.github.nbb.iot.common.constants.NacosConfigConstants;
import io.github.nbb.iot.common.domain.IotProductDO;
import io.github.nbb.iot.gateway.task.ProductTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 胡鹏
 */
@Component
public class ProductStore extends BaseStore<IotProductDO> {

    @Autowired
    private ProductTask productTask;

    private List<IotProductDO> PRODUCT_LIST = new CopyOnWriteArrayList<>();

    public ProductStore() {
        super(NacosConfigConstants.IOT_PRODUCT_DATA_ID, IotProductDO.class);
    }

    @Override
    void receiveDataList(List<IotProductDO> productList) {
        synchronized (this) {
            PRODUCT_LIST.clear();
            PRODUCT_LIST.addAll(productList);
            productTask.reloadTask(productList);
        }
    }

}
