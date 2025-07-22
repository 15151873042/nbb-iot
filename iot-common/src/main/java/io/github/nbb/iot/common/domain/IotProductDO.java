package io.github.nbb.iot.common.domain;

import lombok.Data;

/**
 * iot产品
 * @author 胡鹏
 */
@Data
public class IotProductDO {

    private Long id;

    private String productName;

    /**
     * 采集间隔（单位秒）
     */
    private Integer collectInterval;

    /**
     * 状态（0正常 1停用）
     */
    private String status;
}
