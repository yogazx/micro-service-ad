package com.yoge.ad.service.search.index;

/**
 * DESC 检索系统索引
 *
 * @author You Jia Ge
 * Created Time 2019/5/16
 */
public interface IndexAware<K, V> {

    V get(K key);

    void add(K key, V value);

    void update(K key, V value);

    void delete(K key, V value);
}
