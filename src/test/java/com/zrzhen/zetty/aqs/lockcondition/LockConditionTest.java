package com.zrzhen.zetty.aqs.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionTest {
    private LinkedList<String> queue=new LinkedList<String>();

    private Lock lock = new ReentrantLock();

    private int maxSize = 5;

    private Condition providerCondition = lock.newCondition();

    private Condition consumerCondition = lock.newCondition();

    public void provide(String value){
        try {
            lock.lock();
            while (queue.size() == maxSize) {

                providerCondition.await();
            }
            System.out.println("provider is waiting.."+queue.size());

            queue.add(value);
            consumerCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String consume(){
        String result = null;
        try {
            lock.lock();
            while (queue.size() == 0) {
                consumerCondition.await();
            }
            System.out.println("consumer is waiting.."+queue.size());

            result = queue.poll();
            providerCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return result;
    }

    public static void main(String[] args) {
        LockConditionTest t = new LockConditionTest();
        new Thread(new Provider(t)).start();
        new Thread(new Consumer(t)).start();

    }

}