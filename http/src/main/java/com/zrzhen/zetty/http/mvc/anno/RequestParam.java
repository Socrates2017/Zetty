package com.zrzhen.zetty.http.mvc.anno;

import java.lang.annotation.*;

/**
 * request传入的查询参数，可以根据value名自动注入
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    /**
     * 映射到uri中对应的参数名
     *
     * @return
     */
    String name() default "";


    String defaultValue() default "";

    /**
     * 是否必传
     *
     * @return
     */
    boolean required() default false;

}
