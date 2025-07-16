package io.github.nbb.iot.console.util;

import io.github.nbb.iot.console.domain.PageResult;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author 胡鹏
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {


    public static <S, T> PageResult<T> copyPageResult(PageResult<S> source, Class<T> targetType) {
        return copyPageResult(source, targetType, null);
    }

    public static <S, T> PageResult<T> copyPageResult(PageResult<S> source, Class<T> targetType, Consumer<T> peek) {
        if (source == null) {
            return null;
        }
        List<T> list = copyToList(source.getList(), targetType);
        if (peek != null) {
            list.forEach(peek);
        }
        return new PageResult<>(list, source.getTotal());
    }
}
