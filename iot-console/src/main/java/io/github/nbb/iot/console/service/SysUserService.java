package io.github.nbb.iot.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.UserAllocatedPageDTO;
import io.github.nbb.iot.console.domain.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param dto 用户信息
     * @return 用户信息集合信息
     */
    PageResult<SysUser> listAllocatedPage(UserAllocatedPageDTO dto);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param dto 用户信息
     * @return 用户信息集合信息
     */
    PageResult<SysUser> listUnallocatedPage(UserAllocatedPageDTO dto);
}
