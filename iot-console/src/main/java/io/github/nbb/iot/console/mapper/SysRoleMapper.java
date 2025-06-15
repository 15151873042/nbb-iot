package io.github.nbb.iot.console.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.nbb.iot.console.core.domain.entity.SysRole;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);
}
