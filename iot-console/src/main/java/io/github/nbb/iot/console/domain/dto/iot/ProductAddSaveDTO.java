package io.github.nbb.iot.console.domain.dto.iot;

import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class ProductAddSaveDTO {
    /**
     * 产品名称
     */
    private String productName;

    /**
     * 采集间隔（单位秒）
     */
    private Integer collectInterval;

    /**
     * 备注
     */
    private String remark;

}
