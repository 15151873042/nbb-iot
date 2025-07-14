package io.github.nbb.iot.console.domain.dto.iot;

import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class ProductEditSaveDTO {

    private Long id;
    /**
     * 产品名称
     */
    private String productName;

    /**
     * 备注
     */
    private String remark;

}
