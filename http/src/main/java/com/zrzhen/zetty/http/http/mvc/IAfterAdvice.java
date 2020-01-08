package com.zrzhen.zetty.http.http.mvc;


/**
 * @author chenanlian
 * <p>
 * 后置增强器接口，所有后置增强器都实现此接口
 */
public interface IAfterAdvice<T> {

    /**
     * @param response 从控制类或上一个后置增强器中传入的返回结果
     * @return 此增强器返回的结果，如果是最后一个后置增强器，则作为最终结果返回给客户端
     * 如果返回的类型为ZettyResponse，则直接返回给客户端，其他类型则需与控制类方法上注解的类型保持一致
     */
    public Object after(T response);

}
