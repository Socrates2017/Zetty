package com.zrzhen.idmaker;

import com.zrzhen.idmaker.core.Id;

import java.util.Date;

public class IdUtil {

    private static IdMaker idWorker = new IdMaker(new IdMeta());

    /**
     * 生成一个id
     * @return
     */
    public static long genId() {
        return idWorker.nextId();
    }

    public Id expId(long id) {


        return null;
    }

    public long makeId(long time, long seq) {
        return 0;
    }

    public long makeId(long time, long seq, long machine) {
        return 0;
    }

    public long makeId(long genMethod, long time, long seq, long machine) {
        return 0;
    }

    public long makeId(long type, long genMethod, long time,
                       long seq, long machine) {
        return 0;
    }

    public long makeId(long version, long type, long genMethod,
                       long time, long seq, long machine) {
        return 0;
    }

    public Date transTime(long time) {
        return null;
    }


    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            long id = IdUtil.genId();
            System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }
        System.out.println(System.currentTimeMillis());
    }
}
