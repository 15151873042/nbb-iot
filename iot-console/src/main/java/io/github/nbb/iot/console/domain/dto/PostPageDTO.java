package io.github.nbb.iot.console.domain.dto;

import io.github.nbb.iot.console.domain.PageParam;
import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class PostPageDTO extends PageParam {

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 状态（0正常 1停用）
     */
    private String status;
}
