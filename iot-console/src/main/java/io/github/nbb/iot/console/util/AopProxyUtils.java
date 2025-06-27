package io.github.nbb.iot.console.util;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopContext;

/**
 * @author 胡鹏
 */
public class AopProxyUtils {

    /**
     * 获取aop代理对象
     *
     * @param invoker
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        Object proxy = AopContext.currentProxy();
        if (((Advised) proxy).getTargetSource().getTargetClass() == invoker.getClass()) {
            return (T) proxy;
        }
        return invoker;
    }
}
