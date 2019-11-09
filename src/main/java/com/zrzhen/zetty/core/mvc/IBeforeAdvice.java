package com.zrzhen.zetty.core.mvc;


import com.zrzhen.zetty.core.http.Response;

/**
 * @author chenanlian
 * <p>
 * 前置增强器接口，所有前置增强器都实现此接口
 */
public interface IBeforeAdvice {

    /**
     * 从ThreadLocal中获取ZettyResponse并返回，如果需要拦截则将返回结果的ZettyResponse中的flag设为false
     *
     * @return
     */
    public Response before();

}
