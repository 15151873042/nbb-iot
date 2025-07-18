package io.github.nbb.iot.common.domain;

import lombok.Data;

/**
 * iot产品
 * @author 胡鹏
 */
@Data
public class IotDeviceDO {

    private Long id;

    private String deviceName;

    private Long productId;

    private Long serialId;

    private String serialAddressCode;
}
