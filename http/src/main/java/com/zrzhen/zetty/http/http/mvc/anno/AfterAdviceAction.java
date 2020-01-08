package com.zrzhen.zetty.http.http.mvc.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 处理方法上的后置拦截器注解
 * 注解在Controller类下的uri处理方法上，将会在执行该方法前，执行id为本注解的值id的拦截器
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterAdviceAction {

    String id();

}
