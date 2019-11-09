package com.zrzhen.zetty;

import com.zrzhen.zetty.core.Zetty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenanlian
 */
public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Zetty.run(args);
    }


}
