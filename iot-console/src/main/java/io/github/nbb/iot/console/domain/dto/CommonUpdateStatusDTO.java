package io.github.nbb.iot.console.domain.dto;

import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class CommonUpdateStatusDTO {

    private Long id;

    /**
     * 状态（0正常 1停用）
     */
    private String status;
}
