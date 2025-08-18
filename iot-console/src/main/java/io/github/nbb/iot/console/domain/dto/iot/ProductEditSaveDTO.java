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
     * 采集间隔（单位秒）
     */
    private Integer collectInterval;

    /**
     * 动态脚本代码
     */
    private String dynamicCode;

    /**
     * 备注
     */
    private String remark;

}
