package io.github.nbb.iot.console.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.LoginUser;
import io.github.nbb.iot.console.domain.dto.LoginDTO;
import io.github.nbb.iot.console.domain.entity.SysMenu;
import io.github.nbb.iot.console.domain.vo.LoginVO;
import io.github.nbb.iot.console.service.LoginService;
import io.github.nbb.iot.console.service.SysMenuService;
import io.github.nbb.iot.console.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private SysMenuService menuService;


    /**
     * 登录方法
     *
     * @param loginDTO 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginDTO loginDTO) {
        LoginVO vo = loginService.login(loginDTO);
        return AjaxResult.success(vo);
    }

    @PostMapping("/logout")
    public AjaxResult logout() {
        StpUtil.logout();
        return AjaxResult.success();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser().get();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", loginUser.getSysUser());
        ajax.put("roles", loginUser.getRoles());
        ajax.put("permissions", loginUser.getPermissions());
        return ajax;
    }


    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId().get();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
