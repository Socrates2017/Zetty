package com.zrzhen.zetty.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenanlian
 */
public class ShellUtil {

    private final static Logger log = LoggerFactory.getLogger(ShellUtil.class);

    public static List<String> execute(List<String> command) throws InterruptedException, IOException {
        List<String> results = new ArrayList<String>();
        ProcessBuilder hiveProcessBuilder = new ProcessBuilder(command);
        Process hiveProcess = hiveProcessBuilder.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                hiveProcess.getInputStream()));
        String data = null;
        while ((data = br.readLine()) != null) {
            results.add(data);
        }
        return results;

    }

    public static String exec(String command) throws InterruptedException {
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            log.error("Create runtime false!");
        }
        try {
            pro = runTime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                log.info(line);
                returnString = returnString + line + "\n";
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException e) {
            log.error("shell command execute error:", e);
        }
        return returnString;
    }

}
