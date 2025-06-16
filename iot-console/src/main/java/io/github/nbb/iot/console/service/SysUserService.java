package io.github.nbb.iot.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);
}
