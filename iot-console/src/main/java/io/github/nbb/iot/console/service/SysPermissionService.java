package io.github.nbb.iot.console.service;


import io.github.nbb.iot.console.domain.entity.SysUser;

import java.util.Set;

/**
 * 权限信息 服务层
 *
 * @author ruoyi
 */
public interface SysPermissionService {

    /**
     * 获取菜单数据权限
     *
     * @param userId 用户Id
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(SysUser user);

    /**
     * 获取角色数据权限
     *
     * @param userId 用户Id
     * @return 角色权限信息
     */
    Set<String> getRolePermission(SysUser sysUser);
}
