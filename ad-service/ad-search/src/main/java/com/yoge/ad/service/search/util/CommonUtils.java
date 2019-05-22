package com.yoge.ad.service.search.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Supplier;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/19
 */
@Slf4j
public class CommonUtils {

    /**
     * 若map中不存在传入的key，则用传进来的factory构建一个新的value对象
     * @param key
     * @param map
     * @param factory
     * @param <K>
     * @param <V>
     * @return
     */
    // todo 这里着重理解一下函数式写法
    public static <K, V> V getOrCreateMap(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }
}
