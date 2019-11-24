package com.zrzhen.zetty.aqs.gc;

import java.util.ArrayList;
import java.util.List;

public class GCTest {

    private static final int _10MB = 10 * 1024 * 1024;

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws Exception {
        test();

    }

    /**
     * VM arg： -Xms100m(设置最大堆内存) -Xmx100m(设置初始堆内存)
     * -Xmn50m(设置新生代大小)
     * -XX:+PrintGCDetails(打印GC日志详细信息)
     * -XX:+UseConcMarkSweepGC (采用 cms gc算法)
     * -XX:+UseParNewGC (新生代采用并行GC方式,
     * 高版本的jdk使用了UseConcMarkSweepGC参数时 这个参数会自动开启)
     * -XX:SurvivorRatio=8 (新生代eden区与survivor区空间比例8:1,
     * eden:fromsurvivor:tosurvivor -->8:1:1)
     * -XX:MaxTenuringThreshold=1 (用于控制对象能经历多少次
     * Minor GC(young gc)才晋升到老年代,默认15次)
     * -XX:+PrintTenuringDistribution(输出survivor区幸存对象的年龄分布)
     * -XX:CMSInitiatingOccupancyFraction=68 *(设置老年代空间使用率多少时触发第一次cms *gc,默认68%)
     * @throws InterruptedException
     */
    public static void test() throws InterruptedException {
        List<byte[]> list = new ArrayList<>();
        for (int n = 1; n < 8; n++) {
            byte[] alloc = new byte[_10MB];
            list.add(alloc);
        }
        Thread.sleep(Integer.MAX_VALUE);

    }

}
