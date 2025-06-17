package io.github.nbb.iot.console.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import io.github.nbb.iot.console.constant.Constants;
import io.github.nbb.iot.console.constant.UserConstants;
import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.entity.SysMenu;
import io.github.nbb.iot.console.service.SysMenuService;
import io.github.nbb.iot.console.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @SaCheckPermission("system:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, SecurityUtils.getUserId().get());
        return AjaxResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @SaCheckPermission("system:menu:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        SysMenu detail = menuService.selectMenuById(id);
        return AjaxResult.success(detail);
    }


    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, SecurityUtils.getUserId().get());
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysMenu> menus = menuService.selectMenuList(SecurityUtils.getUserId().get());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @SaCheckPermission("system:menu:add")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StrUtil.startWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @SaCheckPermission("system:menu:edit")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StrUtil.startWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getId().equals(menu.getParentId())) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @SaCheckPermission("system:menu:remove")
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return warn("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return warn("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }

}
