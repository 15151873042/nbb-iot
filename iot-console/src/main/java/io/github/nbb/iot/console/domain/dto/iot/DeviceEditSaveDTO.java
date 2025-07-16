package io.github.nbb.iot.console.domain.dto.iot;

import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class DeviceEditSaveDTO {

    private Long id;

    private String deviceName;

    private Long productId;

    private Long serialId;

    private String serialAddressCode;

    private String remark;

}
