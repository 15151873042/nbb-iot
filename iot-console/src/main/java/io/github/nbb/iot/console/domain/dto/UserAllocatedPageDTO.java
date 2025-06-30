package io.github.nbb.iot.console.domain.dto;

import io.github.nbb.iot.console.domain.PageParam;
import lombok.Data;

/**
 * @author 胡鹏
 */
@Data
public class UserAllocatedPageDTO extends PageParam {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 手机号码
     */
    private String phonenumber;

}
