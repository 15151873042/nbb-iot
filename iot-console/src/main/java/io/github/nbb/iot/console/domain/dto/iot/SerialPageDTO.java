package io.github.nbb.iot.console.domain.dto.iot;

import io.github.nbb.iot.console.domain.PageParam;
import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class SerialPageDTO extends PageParam {

    /**
     * 网关编号
     */
    private String ip;
    /**
     * 网关编号
     */
    private Integer port;
    /**
     * 是否在线
     */
    private Boolean isOnline;
}
