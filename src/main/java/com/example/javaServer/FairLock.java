package com.example.javaServer;/*
 * @Author Administrator
 * @Description 公平锁
 * @Date 2018/12/15/015
 **/

import java.util.ArrayList;
import java.util.List;

class QueueObject {
    private boolean isNotified = false;

    public synchronized  void doWait() throws InterruptedException {
        while (!isNotified) {
            this.wait();
        }
        this.isNotified = false;
    }

    public synchronized void doNotify() {
        this.isNotified = true;
        this.notify();
    }

    public boolean equals(Object o) {
        return this == o;
    }
}
public class FairLock {
    private boolean isLocked = false;
    private Thread lockingThread = null;
    private List<QueueObject> waitingThreads =
            new ArrayList<>();
    public void lock() throws InterruptedException {
        QueueObject queueObject = new QueueObject();
        boolean isLockedForThisThread = true;
        synchronized (this) {
            waitingThreads.add(queueObject);
        }
        //以上表示有线程想要获取锁，需要登记一下，也就是加入到waitingThreads集合中
        while (isLockedForThisThread) {//想要锁线程
            synchronized (this) {
                //waitingThreads.get(0) != queueObject;  表示数组中只有当前进来对象
                isLockedForThisThread = isLocked || waitingThreads.get(0) != queueObject; //当前是否已经有线程占用锁，当前不是它自己
                if (!isLockedForThisThread) { //表示没有被锁定，且当前没有其他竞争者
                    isLocked = true; //表示方法已经被锁定
                    waitingThreads.remove(queueObject); //移除当对象
                    lockingThread = Thread.currentThread();//把当前线程lockingThread值
                    return;
                }
            }
            try {
                queueObject.doWait(); //否则需要等待
            } catch (InterruptedException e) {
                synchronized (this) {
                    waitingThreads.remove(queueObject);
                }
                throw  e;
            }
        }
    }

    /**
     * 解锁
     */
    public synchronized void unlock() {
        if (this.lockingThread != Thread.currentThread()) { //如果不是锁定当前线程
            throw new IllegalMonitorStateException("calling thread has not locked this lock");
        }
        isLocked = false; //解锁
        lockingThread = null;//锁定线程为空
        if (waitingThreads.size() > 0) { //唤醒第一个线程
            waitingThreads.get(0).doNotify();
        }
    }

}
