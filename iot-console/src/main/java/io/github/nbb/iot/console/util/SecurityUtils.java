package io.github.nbb.iot.console.util;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.nbb.iot.console.domain.LoginUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

/**
 * 权限获取工具类
 */
public class SecurityUtils {

    /**
     * 当前会话是否已经登录
     * @return 是否已登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }


    public static LoginUser getLoginUser() {
        return tryGetLoginUser().orElseThrow(() -> NotLoginException.newInstance(StpUtil.TYPE, NotLoginException.DEFAULT_MESSAGE));

    }


    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return getLoginUser().getSysUser().getId();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername() {
        return getLoginUser().getSysUser().getUserName();
    }




    /**
     * 获取请求token
     */
    public static String getToken() {
        return StpUtil.getTokenValue();
    }


    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 获取登录用户信息
     */
    public static Optional<LoginUser> tryGetLoginUser() {
        SaSession session = StpUtil.getSession(false);
        if (ObjectUtil.isNull(session)) {
            return Optional.empty();
        }

        LoginUser loginUser = (LoginUser)session.get(SaSession.USER);
        return Optional.ofNullable(loginUser);

    }
}
