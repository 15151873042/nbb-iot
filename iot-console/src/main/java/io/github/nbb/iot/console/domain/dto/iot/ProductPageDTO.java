package io.github.nbb.iot.console.domain.dto.iot;

import io.github.nbb.iot.console.domain.PageParam;
import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class ProductPageDTO extends PageParam {

    /**
     * 产品名称
     */
    private String productName;
}
