package io.github.nbb.iot.console.domain.vo.iot;

import io.github.nbb.iot.console.domain.entity.iot.IotDevice;
import io.github.nbb.iot.console.domain.entity.iot.IotProduct;
import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class DevicePageVO extends IotDevice {

    private String productName;

    private String serialName;

}
