package com.zrzhen.zetty.core.mvc;


import com.zrzhen.zetty.core.mvc.anno.*;
import com.zrzhen.zetty.core.mvc.exception.AdviceNotFoundException;
import com.zrzhen.zetty.core.mvc.exception.RequestMappingDublicateException;
import com.zrzhen.zetty.core.util.ProUtil;
import com.zrzhen.zetty.core.util.ServerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用来存储整个应用共享的上下文信息，只应该存在一个单例实例，在应用启动时初始化，不能改变
 *
 * @author chenanlian
 */
public class ServerContext {

    private static final Logger log = LoggerFactory.getLogger(ServerContext.class);

    /**
     * 路由到执行方法的映射
     */
    public static Map<String, ControllerMethod> requestMapping = new ConcurrentHashMap<String, ControllerMethod>();

    /**
     * 前增强器id到实例的映射
     */
    public static Map<String, IBeforeAdvice> iBeforeAdviceMap = new ConcurrentHashMap<String, IBeforeAdvice>();

    /**
     * 后增强器id到实例的映射
     */
    public static Map<String, IAfterAdvice> iAfterAdviceMap = new ConcurrentHashMap<String, IAfterAdvice>();


    /**
     * 初始化
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws AdviceNotFoundException
     * @throws RequestMappingDublicateException
     */
    public static void init() throws IllegalAccessException, InstantiationException, ClassNotFoundException, AdviceNotFoundException, RequestMappingDublicateException {
        initAdviceMap();
        initRequestMap();
    }


    /**
     * 初始化增强器映射
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void initAdviceMap() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<String> packages = new ArrayList<String>();
        packages.add(ProUtil.getString("server.scan.advice", null));
        for (String scanPackage : packages) {
            log.info("scanning advice package:{} ", scanPackage);
            String[] classNames = ServerUtil.findClassesInPackage(scanPackage + ".*");
            for (String className : classNames) {

                Class<?> interceptorClass = Class.forName(className);
                BeforeAdvice beforeAdvice = interceptorClass.getAnnotation(BeforeAdvice.class);
                if (beforeAdvice != null) {
                    String id = beforeAdvice.id();
                    if (StringUtils.isBlank(id)) {
                        id = StringUtils.uncapitalize(interceptorClass.getSimpleName());
                    }
                    IBeforeAdvice target = (IBeforeAdvice) interceptorClass.newInstance();
                    iBeforeAdviceMap.put(id, target);
                    log.info("Registering BeforeAdvice id: {}", id);
                }

                /**允许一个advice同时实现前增强和后增强，故此处不放在else中，避免没有加载到*/
                AfterAdvice afterAdvice = interceptorClass.getAnnotation(AfterAdvice.class);
                if (afterAdvice != null) {
                    String id = afterAdvice.id();
                    if (StringUtils.isBlank(id)) {
                        id = StringUtils.uncapitalize(interceptorClass.getSimpleName());
                    }
                    IAfterAdvice target = (IAfterAdvice) interceptorClass.newInstance();
                    iAfterAdviceMap.put(id, target);
                    log.info("Registering AfterAdvice id: {}", id);
                }

            }
        }
        log.info("AdviceMapping  Initialization successfully");
    }


    /**
     * 扫描packet下面所有的映射，初始化mapping
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws RequestMappingDublicateException
     * @throws AdviceNotFoundException
     */
    public static void initRequestMap() throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            RequestMappingDublicateException, AdviceNotFoundException {
        List<String> packages = new ArrayList<String>();
        packages.add(ProUtil.getString("server.scan.controller", null));
        for (String scanPackage : packages) {
            log.info("scanning advice package:{} ", scanPackage);
            String[] classNames = ServerUtil.findClassesInPackage(scanPackage + ".*");
            for (String className : classNames) {
                Class<?> controllerClass = Class.forName(className);
                Controller controller = controllerClass.getAnnotation(Controller.class);
                if (controller == null) {
                    continue;
                }
                String controllerVal = controller.value();
                log.info("Registering controller: {}", controllerVal);
                for (Method method : controllerClass.getDeclaredMethods()) {
                    if (method.getModifiers() == Modifier.PUBLIC) {
                        RequestMapping rm = method.getAnnotation(RequestMapping.class);
                        if (rm != null) {
                            String pathVal = String.valueOf(rm.value());
                            /*兼容控制器注解值不以斜杆开头*/
                            if (!controllerVal.startsWith("/")) {
                                controllerVal = "/" + controllerVal;
                            }

                            /**
                             * 处理url中动态参数{pathVariable}，映射中去掉它
                             */
                            String regex = "\\{([^}]*)\\}$";
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(pathVal);
                            String pathVariable = "";
                            while (matcher.find()) {
                                pathVariable = matcher.group();
                            }
                            if (StringUtils.isNotBlank(pathVariable)) {
                                pathVal = pathVal.substring(0, pathVal.lastIndexOf(pathVariable));

                                if (pathVal.endsWith("/")) {
                                    pathVal = pathVal.substring(0, pathVal.length() - 1);
                                }
                            }

                            /*如果两者皆有，则去除一个*/
                            if (controllerVal.endsWith("/") && pathVal.startsWith("/")) {
                                pathVal = pathVal.replaceFirst("/", "");
                            }

                            String uri = controllerVal + pathVal;
                            if (requestMapping.containsKey(uri)) {
                                throw new RequestMappingDublicateException(uri);
                            }
                            makeAccessible(method);
                            Object target = controllerClass.newInstance();
                            ContentType contentType = method.getAnnotation(ContentType.class);
                            ContentTypeEnum contentTypeEnum;
                            if (contentType != null) {
                                contentTypeEnum = contentType.value();
                            } else {
                                contentTypeEnum = ContentTypeEnum.TEXT;
                            }
                            ControllerMethod controllerMethod = new ControllerMethod(target, method, contentTypeEnum);

                            log.info("uri = {}, route = {},contentType = {}", uri, method.toString(), contentTypeEnum.getType());

                            /**
                             * 解析方法上的增强器注解
                             */
                            BeforeAdviceAction beforeAdviceAction = method.getAnnotation(BeforeAdviceAction.class);
                            if (beforeAdviceAction != null) {
                                String ids = beforeAdviceAction.id();
                                String idArray[] = ids.split(",");
                                for (String id : idArray) {
                                    IBeforeAdvice iBeforeAdvice = iBeforeAdviceMap.get(id);
                                    if (iBeforeAdvice == null) {
                                        String msg = "Advice id is " + id + ", method is " + method.toString();
                                        throw new AdviceNotFoundException(msg);
                                    } else {
                                        controllerMethod.addBeforeAdviceList(iBeforeAdvice);
                                    }
                                }
                            }

                            AfterAdviceAction afterAdviceAction = method.getAnnotation(AfterAdviceAction.class);
                            if (afterAdviceAction != null) {
                                String ids = afterAdviceAction.id();
                                String idArray[] = ids.split(",");
                                for (String id : idArray) {
                                    IAfterAdvice iAfterAdvice = iAfterAdviceMap.get(id);
                                    if (iAfterAdvice == null) {
                                        String msg = "Advice id is " + id + ", method is " + method.toString();
                                        throw new AdviceNotFoundException(msg);
                                    } else {
                                        controllerMethod.addAfterAdviceList(iAfterAdvice);
                                    }
                                }
                            }
                            requestMapping.put(uri, controllerMethod);
                        }
                    }
                }
            }
        }
        log.info("RequestMapping  Initialization successfully");
    }


    /**
     * 通过uri获取controller方法
     *
     * @param uri
     * @return
     */
    public static ControllerMethod getControllerMethod(String uri) {
        return requestMapping.get(uri);
    }


    /**
     * 使方法可以进入
     *
     * @param method
     */
    private static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers())
                || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}

