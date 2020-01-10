package com.zrzhen.zetty.http.http.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.http.http.http.Request;
import com.zrzhen.zetty.http.http.http.Response;
import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.anno.PathVariable;
import com.zrzhen.zetty.http.http.mvc.anno.RequestAnno;
import com.zrzhen.zetty.http.http.mvc.anno.RequestJsonBody;
import com.zrzhen.zetty.http.http.mvc.anno.RequestParam;
import com.zrzhen.zetty.http.http.mvc.exception.RequestBodyShouldNotBeEmptyException;
import com.zrzhen.zetty.http.http.mvc.exception.RequestParamShouldNotBeEmptyException;
import com.zrzhen.zetty.http.http.mvc.exception.UriNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author chenanlian
 * <p>
 * 服务辅助类
 */
public class ServerUtil {

    private final static Logger log = LoggerFactory.getLogger(ServerUtil.class);

    public final static List<String> EMPTY_LIST = new ArrayList<String>(0);


    /**
     * 转换html路径，兼容漏写斜杆的情况
     *
     * @param htmlPath
     * @return
     */
    public static String covPath(String htmlPath) {
        if (htmlPath.startsWith("/")) {
            htmlPath = "html" + htmlPath;
        } else {
            htmlPath = "html/" + htmlPath;
        }
        return htmlPath;
    }




    /**
     * 根据文件相对路径读取字符串
     *
     * @param path
     * @return
     */
    public static String getHtml(String path) {
        byte[] bytes = FileUtil.file2ByteByRelativePath(path);
        return new String(bytes);
    }

    /**
     * 设置html模板
     *
     * @param html 要替换的html
     * @param map  要替换的map
     * @return 替换内容后的html文档
     */
    public static String setModel(String html, Map<String, Object> map) {

        String p = "";
        /*是否是第一次*/
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = "\\$\\{" + entry.getKey() + "}";
            if (first) {
                first = false;
            } else {
                p += "|";
            }
            p += key;
        }
        log.debug("model解析规则为：{}", p);

        Pattern pa = Pattern.compile(p);
        Matcher m = pa.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = "${" + entry.getKey() + "}";
                String value = entry.getValue() == null ? "" : entry.getValue().toString();
                if (m.group().equals(key)) {
                    m.appendReplacement(sb, Matcher.quoteReplacement(value));
                    break;
                }
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }


    /**
     * 初始化jar包启动时环境配置文件后缀
     *
     * @param args
     */
    public static void initEnv(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("server.profiles.active=")) {
                ProUtil.innerEnv = false;
                ProUtil.env = arg.substring(23);
            }
        }
        ProUtil.init();
    }

    /**
     * 获取方法的入参值
     *
     * @param method
     * @param params
     * @param jsonBody
     * @param request
     * @param response
     * @param pathVariable uri中的动态参数
     * @return
     * @throws RequestParamShouldNotBeEmptyException
     * @throws RequestBodyShouldNotBeEmptyException
     */
    public static Object[] getParameterValues(Method method, Map<String, String> params,
                                              JsonNode jsonBody,
                                              Request request,
                                              Response response,
                                              String pathVariable)
            throws RequestParamShouldNotBeEmptyException,
            RequestBodyShouldNotBeEmptyException,
            UriNotFoundException {

        Type[] type = method.getGenericParameterTypes();
        Object[] paramTarget = new Object[type.length];
        for (int i = 0; i < type.length; i++) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Annotation annotation = parameterAnnotations[i][0];
            if (annotation.annotationType() == RequestParam.class) {
                String name = ((RequestParam) parameterAnnotations[i][0]).name();
                String value = params.get(name);
                boolean required = ((RequestParam) parameterAnnotations[i][0]).required();
                if (StringUtils.isBlank(value)) {
                    if (required) {
                        throw new RequestParamShouldNotBeEmptyException(name);
                        /*默认值*/
                    } else {
                        String defaultValue = ((RequestParam) parameterAnnotations[i][0]).defaultValue();
                        if (StringUtils.isNotBlank(defaultValue)) {
                            paramTarget[i] = getParamTarget(defaultValue, type[i]);
                        } else {
                            paramTarget[i] = null;
                        }
                    }
                } else {
                    paramTarget[i] = getParamTarget(value, type[i]);
                }
            } else if (annotation.annotationType() == RequestAnno.class) {
                paramTarget[i] = request;
            } else if (annotation.annotationType() == RequestAnno.class) {
                paramTarget[i] = response;
            } else if (annotation.annotationType() == PathVariable.class) {
                if (StringUtils.isBlank(pathVariable)) {
                    throw new UriNotFoundException();
                } else {
                    paramTarget[i] = getParamTarget(pathVariable, type[i]);
                }
            } else if (annotation.annotationType() == RequestJsonBody.class) {
                boolean required = ((RequestJsonBody) parameterAnnotations[i][0]).required();
                if (required && null == jsonBody) {
                    throw new RequestBodyShouldNotBeEmptyException();
                } else {
                    paramTarget[i] = jsonBody;
                }
            } else {
                paramTarget[i] = null;
            }
        }
        return paramTarget;
    }

    /**
     * 获取参数，转换成对应的子类型
     *
     * @param value
     * @param type
     * @return
     */
    protected static Object getParamTarget(String value, Type type) {
        if (Integer.class == type || StringUtils.equals(type.toString(), "int")) {
            return Integer.parseInt(value);
        } else if (String.class == type) {
            return value;
        } else if (Boolean.class == type || StringUtils.equals(type.toString(), "boolean")) {
            return Boolean.parseBoolean(value);
        } else if (Long.class == type || StringUtils.equals(type.toString(), "long")) {
            return Long.parseLong(value);
        } else if (Short.class == type || StringUtils.equals(type.toString(), "short")) {
            return Short.parseShort(value);
        } else if (Double.class == type || StringUtils.equals(type.toString(), "double")) {
            return Double.parseDouble(value);
        } else if (Float.class == type || StringUtils.equals(type.toString(), "float")) {
            return Float.parseFloat(value);
        } else {
            return value;
        }
    }


    /**
     * 查找指定package下的class
     *
     * @param packageName package名字，允许正则表达式
     * @return 符合要求的classes，全路径名
     * @throws java.io.IOException
     */
    public static String[] findClassesInPackage(String packageName) {
        return findClassesInPackage(packageName, EMPTY_LIST, EMPTY_LIST);
    }

    /**
     * 查找指定package下的class
     *
     * @param packageName package名字，允许正则表达式
     * @param included    包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param excluded    不包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @return 符合要求的classes，全路径名
     * @throws java.io.IOException
     */
    public static String[] findClassesInPackage(String packageName, List<String> included, List<String> excluded) {
        String packageOnly = packageName;
        boolean recursive = false;
        // 递归判断
        if (packageName.endsWith(".*")) {
            packageOnly = packageName.substring(0, packageName.lastIndexOf(".*"));
            recursive = true;
        }

        List<String> vResult = new ArrayList<String>();
        try {
            String packageDirName = packageOnly.replace('.', '/');
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                // 如果是目录结构
                if ("file".equals(protocol)) {
                    findClassesInDirPackage(packageOnly, included, excluded, URLDecoder.decode(url.getFile(), "UTF-8"),
                            recursive, vResult);
                }
                // 如果是jar结构
                else if ("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.charAt(0) == '/') {
                            name = name.substring(1);
                        }
                        if (name.startsWith(packageDirName)) {
                            int idx = name.lastIndexOf('/');
                            if (idx != -1) {
                                packageName = name.substring(0, idx).replace('/', '.');
                            }
                            if ((idx != -1) || recursive) {
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    includeOrExcludeClass(packageName, className, included, excluded, vResult);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        String[] result = vResult.toArray(new String[vResult.size()]);
        return result;
    }

    /**
     * 通过遍历目录的方式查找符合要求的包下的class
     *
     * @param packageName 包名，确定名
     * @param included    包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param excluded    不包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param packagePath 包目录路径
     * @param recursive   是否递归
     * @param classes     结果集
     */
    private static void findClassesInDirPackage(String packageName, List<String> included, List<String> excluded,
                                                String packagePath, final boolean recursive, List<String> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 过滤目录和以class后缀的文件
        File[] dirfiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });

        for (File file : dirfiles) {
            if (file.isDirectory()) {
                // 递归处理
                findClassesInDirPackage(packageName + "." + file.getName(), included, excluded, file.getAbsolutePath(),
                        recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                includeOrExcludeClass(packageName, className, included, excluded, classes);
            }
        }
    }

    /**
     * include exclude过滤
     *
     * @param packageName 包名，确定名
     * @param className   短类名，不包含package前缀，确定名
     * @param included    包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param excluded    不包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param classes     结果集
     */
    private static void includeOrExcludeClass(String packageName, String className, List<String> included,
                                              List<String> excluded, List<String> classes) {
        if (isIncluded(className, included, excluded)) {
            classes.add(packageName + '.' + className);
        } else {
        }
    }

    /**
     * 是否包含
     *
     * @param name     短类名，不包含package前缀，确定名
     * @param included 包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @param excluded 不包含的类名(短类名，不包含package前缀)，允许正则表达式
     * @return include-true,else-false
     */
    private static boolean isIncluded(String name, List<String> included, List<String> excluded) {
        boolean result = false;
        if (included.size() == 0 && excluded.size() == 0) {
            result = true;
        } else {
            boolean isIncluded = find(name, included);
            boolean isExcluded = find(name, excluded);
            if (isIncluded && !isExcluded) {
                result = true;
            } else if (isExcluded) {
                result = false;
            } else {
                result = included.size() == 0;
            }
        }
        return result;

    }

    private static boolean find(String name, List<String> list) {
        for (String regexpStr : list) {
            if (Pattern.matches(regexpStr, name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据文件名来获得媒体类型
     *
     * @param fileName
     * @return
     */
    public static String contentTypeByFileName(String fileName) {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
        if (StringUtils.isBlank(contentType)) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            contentType = ContentTypeEnum.getTypeBySuffix(suffix);
            if (StringUtils.isBlank(contentType)) {
                contentType = ContentTypeEnum.OCTET.getType();
            }
        }
        return contentType;
    }
}
