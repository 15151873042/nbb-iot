package io.github.nbb.iot.console.domain.dto;

import io.github.nbb.iot.console.domain.PageParam;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 胡鹏
 */
@Data
public class RolePageDTO extends PageParam {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;


}
