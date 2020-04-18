package com.zrzhen.idmaker;

public class IdUtil {

    private static IdMaker idWorker = new IdMaker(new IdMeta());

    /**
     * 生成一个id
     *
     * @return
     */
    public static long genId() {
        return idWorker.nextId();
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
