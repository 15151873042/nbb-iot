package io.github.nbb.iot.console.domain.dto.iot;

import io.github.nbb.iot.console.domain.PageParam;
import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class DevicePageDTO extends PageParam {

    /**
     * 设备名称
     */
    private String deviceName;

    private Long productId;

    private Long serialId;
}
