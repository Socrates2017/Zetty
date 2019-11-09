package com.zrzhen.zetty.core.mvc.anno;

import java.lang.annotation.*;

/**
 * request传入的查询参数，可以根据value名自动注入
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {

    String value() default "";

}
