package com.java.match.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解：
 * 关键字:@interface
 *
 * 元注解:
 * 什么是元注解？ -> 注解注解的注解
 * @Documented作用：自定义的注解会声明到api的文档中
 *
 * @Retention作用：标注该注解作用的范围
 *  RetentionPolicy.SOURCE:表示自定义的注解可以存活在java源文件中
 *  RetentionPolicy.CLASS:表示自定义的注解可以存活在.class文件中
 *  RetentionPolicy.RUNTIME:运行时保留注解（如果需要反射操作注解，则必选添加则一项）
 *
 * @Target作用：注解可以标注的目标位置
 *  ElementType.ANNOTATION_TYPE:表示该注解可以标注其他的注解
 *  ElementType.CONSTRUCTOR:表示该注解可以标注构造函数
 *  ElementType.FIELD:表示该注解可以标注到属性上
 *  ElementType.LOCAL_VARIABLE:表示该注解可以标注到局部变量上
 *  ElementType.METHOD:表示该注解可以标注到方法上
 *  ElementType.PACKAGE:表示该注解可以标注到包上
 *  ElementType.PARAMETER:表示该注解可以标注到方法的参数上
 *  ElementType.TYPE:表示该注解可以标注到类、接口、枚举类上
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ITable {

    /**
     * 注解的内容格式：
     * 方法的返回值类型[] 方法名() [default value];
     *
     * 注意：如果一个方法的名称为value，则调用该方法时，可以省略名称
     */
    String value();
}
