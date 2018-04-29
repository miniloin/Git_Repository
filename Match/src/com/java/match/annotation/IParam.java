package com.java.match.annotation;

import java.lang.annotation.*;

/**
 * RetentionPolicy.RUNTIME:运行时保留注解（如果需要反射操作注解，则必选添加则一项）
 * ElementType.FIELD:表示该注解可以标注到属性上
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IParam {
    String value();
}
