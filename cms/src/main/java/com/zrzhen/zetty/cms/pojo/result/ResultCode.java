package com.zrzhen.zetty.cms.pojo.result;


/**
 * @author chenanlian
 * <p>
 * 返回码规范
 * <p>
 * 返回码分两类，一类与http返回码一致，如404,503。一类为自定义返回码。下面定义自定义返回码。
 * <p>
 * 返回码由三部分组成：错误类型1+错误类型2+错误类型3。
 * <p>
 * 错误类型1区分错误是系统错误，还是业务错误，占一位。1表示成功，2表示系统错误，3表示业务错误，9表示未知或未归类类型。
 * <p>
 * 错误类型2是错误类型1下的再分类，占两位，其中99表示未知或未归类类型。
 * <p>
 * 错误类型3表示最细粒度的错误，占两位，其中99表示未知或未归类类型。
 * <p>
 * 特殊码：99999表示未知的错误。10000表示成功的请求。
 */

public enum ResultCode {

    /**
     * 特殊码
     */
    SUCCESS(10000, "成功"),
    FAIL(99999, "失败"),

    /**
     * http返回类型
     */
    TIMEOUT(504, "请求超时"),
    NOTFOUND(404, "资源不存在"),

    /**
     * 系统错误
     */
    SERVICE_UNACCESSABLE(29999, "服务器异常"),
    METHOD_ARGUMENT_NOT_VALID(20000, "MethodArgumentNotValidException"),
    HTTP_MESSAGE_NOT_READABLE(20001, "HttpMessageNotReadableException"),
    HTTP_RUNTIME_EXCEPTION(20002, "RuntimeException"),
    IO_EXCEPTION(20200, "IO异常"),
    SYSTEM_DATA_ERROR(20201, "服务器数据异常"),


    /**
     * 业务错误
     */
    INVALID_ARGS(30000, "无效参数"),
    ARG_NEED(30001, "请求中缺少必须传的参数"),
    HTTPERROR(30100, "第三方网络请求异常"),
    INSUFFICIENT_PRIVILEGES(30102, "权限不足"),


    /**
     * SSO相关
     */
    SSO_FAIL(40100, "单点登录失败"),
    SESSION_NULL_CLIENT(40101, "客户端缺失session"),
    SESSION_NULL_SERVER(40102, "服务端缺失session"),
    SESSION_LOGOUT(40103, "未登录状态"),
    SESSION_NULL_USER(40104, "SSO账户为空"),
    SESSION_EXIST_EMAIL(40105, "邮箱已注册"),
    USER_NOT_ADMIN(40106, "非管理员账户，没有权限操作"),



    /**
     * 验证码相关
     */
    CAPTCHA_WRONG(50101, "验证码错误"),
    CAPTCHA_NULL_CODEID(50102, "服务端codeId为空"),

    ;

    private int code;

    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
