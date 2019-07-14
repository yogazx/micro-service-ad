package com.yoge.ad.service.search.mybatis;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 非持久化字段，自动生成SQL时会过滤掉该字段
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient {
}