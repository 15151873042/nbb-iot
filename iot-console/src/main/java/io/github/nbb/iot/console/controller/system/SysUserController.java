package io.github.nbb.iot.console.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.UserPageDTO;
import io.github.nbb.iot.console.domain.entity.SysDept;
import io.github.nbb.iot.console.domain.entity.SysRole;
import io.github.nbb.iot.console.domain.entity.SysUser;
import io.github.nbb.iot.console.service.SysDeptService;
import io.github.nbb.iot.console.service.SysPostService;
import io.github.nbb.iot.console.service.SysRoleService;
import io.github.nbb.iot.console.service.SysUserService;
import io.github.nbb.iot.console.util.SecurityUtils;
import io.github.nbb.iot.console.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysDeptService deptService;

    @Autowired
    private SysPostService postService;

    /**
     * 获取用户列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/list")
    public AjaxResult list(UserPageDTO dto) {
        PageResult<SysUser> result = userService.listPage(dto);
        return AjaxResult.success(result);
    }

//    @SaCheckPermission("system:user:export")
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SysUser user) {
//        List<SysUser> list = userService.selectUserList(user);
//        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//        util.exportExcel(response, list, "用户数据");
//    }

//    @SaCheckPermission("system:user:import")
//    @PostMapping("/importData")
//    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
//        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//        List<SysUser> userList = util.importExcel(file.getInputStream());
//        String operName = getUsername();
//        String message = userService.importUser(userList, updateSupport, operName);
//        return success(message);
//    }

//    @PostMapping("/importTemplate")
//    public void importTemplate(HttpServletResponse response) {
//        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//        util.importTemplateExcel(response, "用户数据");
//    }
//

    /**
     * 根据用户编号获取详细信息
     */
    @SaCheckPermission("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        if (ObjectUtil.isNotNull(userId)) {
//            userService.checkUserDataScope(userId);
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getId).collect(Collectors.toList()));
        }
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        return ajax;
    }

    /**
     * 新增用户
     */
    @SaCheckPermission("system:user:add")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        deptService.checkDeptDataScope(user.getDeptId());
        roleService.checkRoleDataScope(user.getRoleIds());
        if (!userService.checkUserNameUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }


    /**
     * 修改用户
     */
    @SaCheckPermission("system:user:edit")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
//        userService.checkUserDataScope(user.getId());
        deptService.checkDeptDataScope(user.getDeptId());
        roleService.checkRoleDataScope(user.getRoleIds());
        if (!userService.checkUserNameUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }
//
//    /**
//     * 删除用户
//     */
//    @SaCheckPermission("system:user:remove")
//    @DeleteMapping("/{userIds}")
//    public AjaxResult remove(@PathVariable Long[] userIds) {
//        if (ArrayUtils.contains(userIds, getUserId())) {
//            return error("当前用户不能删除");
//        }
//        return toAjax(userService.deleteUserByIds(userIds));
//    }
//
//    /**
//     * 重置密码
//     */
//    @SaCheckPermission("system:user:resetPwd")
//    @PutMapping("/resetPwd")
//    public AjaxResult resetPwd(@RequestBody SysUser user) {
//        userService.checkUserAllowed(user);
//        userService.checkUserDataScope(user.getUserId());
//        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
//        user.setUpdateBy(getUsername());
//        return toAjax(userService.resetPwd(user));
//    }
//
//    /**
//     * 状态修改
//     */
//    @SaCheckPermission("system:user:edit")
//    @PutMapping("/changeStatus")
//    public AjaxResult changeStatus(@RequestBody SysUser user) {
//        userService.checkUserAllowed(user);
//        userService.checkUserDataScope(user.getUserId());
//        user.setUpdateBy(getUsername());
//        return toAjax(userService.updateUserStatus(user));
//    }
//
//    /**
//     * 根据用户编号获取授权角色
//     */
//    @SaCheckPermission("system:user:query")
//    @GetMapping("/authRole/{userId}")
//    public AjaxResult authRole(@PathVariable("userId") Long userId) {
//        AjaxResult ajax = AjaxResult.success();
//        SysUser user = userService.selectUserById(userId);
//        List<SysRole> roles = roleService.selectRolesByUserId(userId);
//        ajax.put("user", user);
//        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
//        return ajax;
//    }
//
//    /**
//     * 用户授权角色
//     */
//    @SaCheckPermission("system:user:edit")
//    @PutMapping("/authRole")
//    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
//        userService.checkUserDataScope(userId);
//        roleService.checkRoleDataScope(roleIds);
//        userService.insertUserAuth(userId, roleIds);
//        return success();
//    }

    /**
     * 获取部门树列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/deptTree")
    public AjaxResult deptTree(SysDept dept) {
        return success(deptService.selectDeptTreeList(dept));
    }
}
