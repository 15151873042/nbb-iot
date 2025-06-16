package io.github.nbb.iot.console.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import io.github.nbb.iot.console.domain.entity.SysRole;
import io.github.nbb.iot.console.domain.entity.SysUser;
import io.github.nbb.iot.console.service.SysMenuService;
import io.github.nbb.iot.console.service.SysPermissionService;
import io.github.nbb.iot.console.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author ruoyi
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {
    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param userId 用户Id
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            return Collections.singleton("admin");
        } else {
            return roleService.selectRolePermissionByUserId(user.getId());
        }
    }

    /**
     * 获取菜单数据权限
     *
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(SysUser user) {

        // 管理员拥有所有权限
        if (user.isAdmin()) {
            return Collections.singleton("*:*:*");
        }

        // 未分配角色或分配角色全部被禁用
        List<SysRole> roles = user.getRoles();
        if (CollectionUtil.isEmpty(roles)) {
            return Collections.emptySet();
        }

        // 多角色设置permissions属性，以便数据权限匹配权限
        Set<String> perms = new HashSet<>();
        for (SysRole role : roles) {
            Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getId());
            role.setPermissions(rolePerms);
            perms.addAll(rolePerms);
        }
        return perms;

    }
}
