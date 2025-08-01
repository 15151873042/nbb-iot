package io.github.nbb.iot.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 串口服务器
 * @author 胡鹏
 */
@EqualsAndHashCode(of = {"ip", "port"})
@Data
public class IotSerialDO {

    private Long id;

    private String ip;

    private Integer port;
}
