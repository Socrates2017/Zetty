package com.zrzhen.zetty.core.mvc.exception;


/**
 * @author chenanlian
 * 增强器找不到异常
 * 如果方法上注解了某个不存在的增强器id，则在启动过程中抛出此异常，并启动失败
 */
public class AdviceNotFoundException extends Exception {


    public AdviceNotFoundException() {
        super();
    }

    public AdviceNotFoundException(String message) {
        super(message);
    }

    public AdviceNotFoundException(Throwable cause) {
        super(cause);
    }

    public AdviceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
