package io.github.nbb.iot.console.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 胡鹏
 */
@Component
@ConfigurationProperties(prefix = "nbb")
public class NbbConfig {

    /** 验证码类型 */
    private static String captchaType;

    public void setCaptchaType(String captchaType) {
        NbbConfig.captchaType = captchaType;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

}
