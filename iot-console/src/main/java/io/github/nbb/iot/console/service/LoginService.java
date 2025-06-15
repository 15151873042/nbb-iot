package io.github.nbb.iot.console.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.nbb.iot.console.constant.CacheConstants;
import io.github.nbb.iot.console.core.domain.LoginUser;
import io.github.nbb.iot.console.core.domain.dto.LoginDTO;
import io.github.nbb.iot.console.core.domain.entity.SysUser;
import io.github.nbb.iot.console.exception.CaptchaException;
import io.github.nbb.iot.console.exception.CaptchaExpireException;
import io.github.nbb.iot.console.framework.redis.RedisCache;
import io.github.nbb.iot.console.framework.web.exception.ServiceException;
import io.github.nbb.iot.console.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Slf4j
@Component
public class LoginService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录验证
     * @return 结果
     */
    public String login(LoginDTO loginDTO) {
        // 验证码校验
        validateCaptcha(loginDTO.getUsername(), loginDTO.getCode(), loginDTO.getUuid());

        SysUser sysUser = sysUserService.selectUserByUserName(loginDTO.getUsername());
        if (ObjectUtil.isNull(sysUser)) {
            log.info("登录用户：{} 不存在.", loginDTO.getUsername());
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }

        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(sysUser);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);

        // 密码校验

        // 用户登陆
        StpUtil.login(sysUser.getId());
        // 用户信息放入session
        SaSession session = StpUtil.getSession();
        session.set(SaSession.USER, loginUser);
        session.set(SaSession.PERMISSION_LIST, permissions);

        String tokenValue = StpUtil.getTokenValue();
        return tokenValue;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = true; // TODO 从数据库获取
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }


}
