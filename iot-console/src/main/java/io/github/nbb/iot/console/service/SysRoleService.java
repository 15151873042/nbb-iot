package io.github.nbb.iot.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.entity.SysRole;

import java.util.Set;

/**
 * 角色业务层
 *
 * @author ruoyi
 */
public interface SysRoleService extends IService<SysRole> {

    Set<String> selectRolePermissionByUserId(Long id);
}
