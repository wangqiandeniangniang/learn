package com.example.javaServer.thread;/*
 * @Author Administrator
 * @Description //TODO
 * @Date 2018/12/15/015
 **/

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
    public static void main(String[] args) {
        final Business business = new Business();
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadExecute(business, "sub");
            }
        }).start();
    }

    private static void threadExecute(Business business, String sub) {
        for (int i = 0; i < 100; i++) {
            try {
                if ("main".equals(sub)) {
                    business.main(i);
                } else {
                    business.sub(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Business{
    private  boolean bool = true;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void main(int loop) throws  InterruptedException {
        lock.lock();
        try {
            while (bool) {
                condition.await();// this.wait();
            }
            for (int i = 0; i < 100; i++) {
                System.out.println("main thread seq of "+ i + ", loop of" + loop);
            }
            bool = true;
            condition.signal(); // this.notify();
        }finally {
            lock.unlock();
        }
    }

    public void sub(int loop) throws  InterruptedException {

        lock.lock();
        try {
            while (!bool) {
                condition.await();//this.wait();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("sub thread seq of " + i + ", loop of " + loop);
            }
            bool = false;
            condition.signal();//this.notify()
        } finally {
            lock.unlock();

        }
    }

}
