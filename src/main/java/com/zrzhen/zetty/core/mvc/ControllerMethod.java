package com.zrzhen.zetty.core.mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author chenanlian
 * 路由执行的方法
 */
public class ControllerMethod {


    /**
     * 执行类
     */
    private Object target;

    /**
     * 执行方法
     */
    private Method method;

    /**
     * 返回内容类型
     */
    private ContentTypeEnum contentType;

    /**
     * 前增强拦截器列表
     */
    private List<IBeforeAdvice> beforeAdviceList = new ArrayList<IBeforeAdvice>();


    /**
     * 后增强拦截器列表
     */
    private List<IAfterAdvice> afterAdviceList = new ArrayList<IAfterAdvice>();

    /**
     * @param target
     * @param method
     */
    public ControllerMethod(Object target, Method method) {
        this.target = target;
        this.method = method;
    }


    public ControllerMethod(Object target, Method method, ContentTypeEnum contentType) {
        this.target = target;
        this.method = method;
        this.contentType = contentType;
    }

    public Object call(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public ContentTypeEnum getContentType() {
        return contentType;
    }

    public void setContentType(ContentTypeEnum contentType) {
        this.contentType = contentType;
    }


    public List<IBeforeAdvice> getBeforeAdviceList() {
        return beforeAdviceList;
    }


    public void addBeforeAdviceList(IBeforeAdvice iBeforeAdvice) {
        beforeAdviceList.add(iBeforeAdvice);
    }

    public List<IAfterAdvice> getAfterAdviceList() {
        return afterAdviceList;
    }

    public void addAfterAdviceList(IAfterAdvice iAfterAdvice) {
        afterAdviceList.add(iAfterAdvice);
    }
}
