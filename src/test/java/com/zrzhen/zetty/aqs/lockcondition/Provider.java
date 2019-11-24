package com.zrzhen.zetty.aqs.lockcondition;

public class Provider implements Runnable {
    LockConditionTest t;

    int count = 0;

    public Provider(LockConditionTest t) {
        this.t = t;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            t.provide(String.valueOf(count));
            count++;
            System.out.println("provider:" + count);
        }
    }

}
