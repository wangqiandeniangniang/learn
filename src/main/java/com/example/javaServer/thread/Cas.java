package com.example.javaServer.thread;/*
 * @Author Administrator
 * @Description CAS 测试 compare and set
 * @Date 2018/12/15/015
 **/

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Cas {

    private int seed;
    public Cas(int s) {
        this.seed = s;
    }

    public synchronized int nextInt(int n) {
        int s = seed;
        seed = new Random().nextInt();
        return s % n;
    }

}

class RandomUsingAtomic {
    private final AtomicInteger seed;

    public RandomUsingAtomic(int s) {
        seed = new AtomicInteger(s);
    }

    public int nextInt(int n) {
        for (; ; ) {
            int s = seed.get();
            int nexts = new Random().nextInt();
            if (seed.compareAndSet(s, nexts)) { //使用了CAS
                return s % n;
            }
        }
    }
}
