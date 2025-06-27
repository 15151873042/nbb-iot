package io.github.nbb.iot.console.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.RolePageDTO;
import io.github.nbb.iot.console.domain.entity.SysDept;
import io.github.nbb.iot.console.domain.entity.SysRole;
import io.github.nbb.iot.console.domain.entity.SysUserRole;
import io.github.nbb.iot.console.service.SysDeptService;
import io.github.nbb.iot.console.service.SysPermissionService;
import io.github.nbb.iot.console.service.SysRoleService;
import io.github.nbb.iot.console.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService roleService;

//    @Autowired
//    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysDeptService deptService;

    @SaCheckPermission("system:role:list")
    @GetMapping("/list")
    public AjaxResult list(RolePageDTO role) {
        PageResult<SysRole> result = roleService.listPage(role);
        return AjaxResult.success(result);
    }

//    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('system:role:export')")
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SysRole role)
//    {
//        List<SysRole> list = roleService.selectRoleList(role);
//        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
//        util.exportExcel(response, list, "角色数据");
//    }
//

    /**
     * 根据角色编号获取详细信息
     */
    @SaCheckPermission("system:role:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @SaCheckPermission("system:role:add")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(getUsername());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());
        if (!roleService.checkRoleNameUnique(role)) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(getUsername());

        return toAjax(roleService.updateRole(role));
    }

    /**
     * 修改保存数据权限
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());
        return toAjax(roleService.authDataScope(role));
    }


    /**
     * 状态修改
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());
        role.setUpdateBy(getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @SaCheckPermission("system:role:remove")
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @SaCheckPermission("system:role:query")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return success(roleService.selectRoleAll());
    }

//    /**
//     * 查询已分配用户角色列表
//     */
//    @SaCheckPermission("system:role:list")
//    @GetMapping("/authUser/allocatedList")
//    public TableDataInfo allocatedList(SysUser user)
//    {
//        startPage();
//        List<SysUser> list = userService.selectAllocatedList(user);
//        return getDataTable(list);
//    }
//
//    /**
//     * 查询未分配用户角色列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:role:list')")
//    @GetMapping("/authUser/unallocatedList")
//    public TableDataInfo unallocatedList(SysUser user)
//    {
//        startPage();
//        List<SysUser> list = userService.selectUnallocatedList(user);
//        return getDataTable(list);
//    }
//

    /**
     * 取消授权用户
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @SaCheckPermission("system:role:edit")
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 获取对应角色部门树列表
     */
    @SaCheckPermission("system:role:query")
    @GetMapping(value = "/deptTree/{roleId}")
    public AjaxResult deptTree(@PathVariable("roleId") Long roleId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
        return ajax;
    }
}
