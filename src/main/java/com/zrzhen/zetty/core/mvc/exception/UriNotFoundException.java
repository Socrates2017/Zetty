package com.zrzhen.zetty.core.mvc.exception;

/**
 * @author chenanlian
 * uri找不到异常，即404
 */
public class UriNotFoundException extends Exception {


    @Override
    public String getMessage() {
        return "uri not found.";
    }
}
