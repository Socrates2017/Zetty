package com.zrzhen.zetty.aqs.lockcondition;

public class Consumer implements Runnable {

    LockConditionTest t;

    public Consumer(LockConditionTest t) {
        this.t = t;
    }

    @Override
    public void run() {

        for (int i = 0; i < 100; i++) {
            String result = t.consume();
            System.out.println(result);

        }
    }
}
