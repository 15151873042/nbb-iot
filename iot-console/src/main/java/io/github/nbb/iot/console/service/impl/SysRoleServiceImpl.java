package io.github.nbb.iot.console.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.nbb.iot.console.domain.entity.SysRole;
import io.github.nbb.iot.console.mapper.SysRoleMapper;
import io.github.nbb.iot.console.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> roles = this.getBaseMapper().selectRolePermissionByUserId(userId);

        return roles.stream()
                .flatMap(role -> Arrays.stream(role.getRoleKey().trim().split(",")))
                .collect(Collectors.toSet());
    }
}
