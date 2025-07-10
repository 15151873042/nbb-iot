package io.github.nbb.iot.console.domain.dto.iot;

import io.github.nbb.iot.console.domain.PageParam;
import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class SerialAddSaveDTO {
    /**
     * ip
     */
    private String ip;
    /**
     * ip
     */
    private Integer port;

    /**
     * 备注
     */
    private String remark;

}
