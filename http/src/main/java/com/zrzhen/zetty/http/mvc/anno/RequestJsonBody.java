package com.zrzhen.zetty.http.mvc.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * controller 方法参数，可以根据value名自动注入
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestJsonBody {
    /**
     * 是否必传
     * @return
     */
    boolean required() default true;
}
