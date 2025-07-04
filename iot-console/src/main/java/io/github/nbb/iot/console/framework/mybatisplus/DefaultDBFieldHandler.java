package io.github.nbb.iot.console.framework.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.github.nbb.iot.console.domain.BaseEntity;
import io.github.nbb.iot.console.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 通用参数填充实现类
 *
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author 胡鹏
 */
@Component
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();

            LocalDateTime current = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseEntity.getCreateTime())) {
                baseEntity.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(current);
            }

            boolean isLogin = SecurityUtils.isLogin();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if (isLogin && Objects.isNull(baseEntity.getCreateBy())) {
                baseEntity.setCreateBy(SecurityUtils.getUsername());
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (isLogin && Objects.isNull(baseEntity.getUpdateBy())) {
                baseEntity.setUpdateBy(SecurityUtils.getUsername());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("updateBy", metaObject);
        boolean isLogin = SecurityUtils.isLogin();
        if (isLogin && Objects.isNull(modifier)) {
            setFieldValByName("updater", SecurityUtils.getUsername(), metaObject);
        }
    }
}
