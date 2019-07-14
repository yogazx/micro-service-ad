package com.yoge.ad.service.search.mybatis;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    /** 表名 **/
    String table() default "";

    /** 主键字段名 **/
    String primaryKey() default "AutoId";

    /** 版本控制字段名 **/
    String versionControl() default "";

}
