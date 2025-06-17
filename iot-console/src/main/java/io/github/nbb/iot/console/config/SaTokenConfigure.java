package io.github.nbb.iot.console.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 胡鹏
 */
@Component
public class SaTokenConfigure implements WebMvcConfigurer {

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，如果不配置拦截器注解校验将失效
        registry
                .addInterceptor(new SaInterceptor(handle -> {
                    SaRouter.match("/**")
                            .notMatch("/login", "/captchaImage", "/logout")
                            .check(r -> StpUtil.checkLogin());
                }))
                .addPathPatterns("/**");
    }
}
