package com.example.javaServer;/*
 * @Author Administrator
 * @Description 生产消费者
 * @Date 2018/12/15/015
 **/

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProductQueue<T> {
    private final  T[] items;

    private final Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    private int head, tail, count;

    public ProductQueue(int maxSize) {
        items = (T[]) new Object[maxSize];
    }
    public ProductQueue() {
        this(10);
    }

    public void put(T t) throws  InterruptedException {
        System.out.println("put = " + t.toString());
        lock.lock();
        try {
            while (count == getCapacity()) {
                notFull.await();
            }
            items[tail] = t;
            if (++tail == getCapacity()) { //初始到开始位置
                tail = 0;
            }
            ++count;//计数
            notEmpty.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public T take() throws  InterruptedException {

        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            T ret = items[head];
            System.out.println("take" + ret.toString());
            items[head] = null; //GC
            if (++head == getCapacity()) {//初始开始位置
                head = 0;
            }
            --count;
            notFull.signalAll();
            return ret;
        } finally {
            lock.unlock();
        }
    }

    public int getCapacity() {
        return items.length;
    }

    public int size() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
