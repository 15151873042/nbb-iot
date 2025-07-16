package io.github.nbb.iot.console.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import io.github.nbb.iot.console.domain.PageResult;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author 胡鹏
 */
public class CollUtil extends cn.hutool.core.collection.CollUtil{
    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc) {
        return convertMap(from, keyFunc, Function.identity());
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1);
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction) {
        return convertMap(from, keyFunc, valueFunc, mergeFunction, HashMap::new);
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction, Supplier<? extends Map<K, V>> supplier) {
        if (CollUtil.isEmpty(from)) {
            return Collections.emptyMap();
        }
        return from.stream().collect(Collectors.toMap(keyFunc, valueFunc, mergeFunction, supplier));
    }



    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func) {
        return from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (CollUtil.isEmpty(from)) {
            return Collections.emptyList();
        }
        return from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <T, U> PageResult<U> convertPage(PageResult<T> from, Function<T, U> func) {
        if (ArrayUtil.isEmpty(from)) {
            return new PageResult<>(from.getTotal());
        }
        return new PageResult<>(convertList(from.getList(), func), from.getTotal());
    }
}
