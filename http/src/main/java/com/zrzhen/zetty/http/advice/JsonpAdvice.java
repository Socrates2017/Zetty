package com.zrzhen.zetty.http.advice;

import com.zrzhen.zetty.http.http.http.Request;
import com.zrzhen.zetty.http.http.mvc.IAfterAdvice;
import com.zrzhen.zetty.http.http.mvc.anno.AfterAdvice;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenanlian
 * <p>
 * jsonp增强器，返回jsonp
 */
@AfterAdvice(id = "jsonpAdvice")
public class JsonpAdvice implements IAfterAdvice<String> {

    private final static Logger log = LoggerFactory.getLogger(JsonpAdvice.class);


    @Override
    public String after(String result) {
        Request request = Request.get();
        String callback = request.getParameters().get("callback");
        if (StringUtils.isBlank(callback)) {
            return result;
        } else {
            return callback + "(" + result + ");";
        }
    }
}
