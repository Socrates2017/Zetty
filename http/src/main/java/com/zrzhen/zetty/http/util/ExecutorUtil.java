package com.zrzhen.zetty.http.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenanlian
 */
public class ExecutorUtil {

    private static final Logger log = LoggerFactory.getLogger(ExecutorUtil.class);


    /**
     * 线程池
     */
    public static ThreadPoolExecutor readExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() + 1,
            1000,
            1,
            TimeUnit.MINUTES,
            new SynchronousQueue<>(),
            new ThreadFactory() {
                private AtomicInteger count = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    String threadName = "readExecutor-" + count.addAndGet(1);
                    t.setName(threadName);
                    return t;
                }
            },
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                    if (!e.isShutdown()) {
                        r.run();
                    }
                }
            }
    );
}
