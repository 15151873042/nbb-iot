package io.github.nbb.iot.console.util;

import io.github.nbb.iot.console.domain.PageResult;

import java.util.List;

/**
 * @author 胡鹏
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {


    public static <S, T> PageResult<T> copyPageResult(PageResult<S> source, Class<T> targetType) {
        if (source == null) {
            return null;
        }

        List<T> list = copyToList(source.getList(), targetType);
        return new PageResult<>(list, source.getTotal());
    }
}
