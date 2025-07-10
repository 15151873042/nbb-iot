package io.github.nbb.iot.console.domain.dto.iot;

import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class SerialEditSaveDTO {

    private Long id;
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
