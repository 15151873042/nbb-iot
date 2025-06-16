package io.github.nbb.iot.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.entity.SysMenu;
import io.github.nbb.iot.console.domain.vo.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author ruoyi
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVo> buildMenus(List<SysMenu> menus);
}
