package io.github.nbb.iot.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.TreeSelect;
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

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(Long userId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(SysMenu menu, Long userId);


    /**
     * 根据菜单ID查询信息
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    SysMenu selectMenuById(Long id);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean checkMenuNameUnique(SysMenu menu);

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysMenu menu);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int updateMenu(SysMenu menu);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * 删除菜单管理信息
     *
     * @param id 菜单ID
     * @return 结果
     */
    public int deleteMenuById(Long id);
}
