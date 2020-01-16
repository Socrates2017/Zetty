package com.zrzhen.zetty.http.mvc.anno;

import java.lang.annotation.Annotation;

/**
 * @author chenanlian
 */
public final class AnnoUtil {

    private AnnoUtil() {

    }

    /**
     * 返回注解
     *
     * @param targetAnnotation 被判断的注解
     * @param annotatedType    被注解的类
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A findAnnotation(final Class<A> targetAnnotation, final Class<?> annotatedType) {

        A foundAnnotation = annotatedType.getAnnotation(targetAnnotation);
        if (foundAnnotation == null) {
            for (Annotation annotation : annotatedType.getAnnotations()) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(targetAnnotation)) {
                    foundAnnotation = annotationType.getAnnotation(targetAnnotation);
                    break;
                }
            }
        }
        return foundAnnotation;
    }

    /**
     * 判断是否存在注解
     *
     * @param targetAnnotation
     * @param annotatedType
     * @return
     */
    public static boolean isAnnotationPresent(final Class<? extends Annotation> targetAnnotation, final Class<?> annotatedType) {
        return findAnnotation(targetAnnotation, annotatedType) != null;
    }


}
