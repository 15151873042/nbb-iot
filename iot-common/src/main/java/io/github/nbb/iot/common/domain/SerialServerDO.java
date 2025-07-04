package io.github.nbb.iot.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 串口服务器
 * @author 胡鹏
 */
@EqualsAndHashCode(of = {"host", "port"})
@Data
public class SerialServerDO {

    private String id;

    private String host;

    private Integer port;
}
