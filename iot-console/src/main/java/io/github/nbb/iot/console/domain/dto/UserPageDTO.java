package io.github.nbb.iot.console.domain.dto;

import io.github.nbb.iot.console.domain.PageParam;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 胡鹏
 */
@Data
public class UserPageDTO extends PageParam {

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 部门ID
     */
    private Long deptId;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
