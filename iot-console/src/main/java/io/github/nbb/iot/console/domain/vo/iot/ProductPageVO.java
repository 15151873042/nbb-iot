package io.github.nbb.iot.console.domain.vo.iot;

import io.github.nbb.iot.console.domain.PageParam;
import io.github.nbb.iot.console.domain.entity.iot.IotProduct;
import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class ProductPageVO extends IotProduct {

    /**
     * 设备数量
     */
    private Integer deviceCount;

}
