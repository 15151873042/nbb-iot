package io.github.nbb.iot.console.domain.dto.iot;

import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class DeviceEditSaveDTO {

    private Long id;
    /**
     * 设备名称
     */
    private String deviceName;

    private Long productId;

    private Long serialId;

    /**
     * 备注
     */
    private String remark;

}
