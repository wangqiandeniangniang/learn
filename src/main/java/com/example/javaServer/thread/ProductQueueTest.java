package com.example.javaServer.thread;/*
 * @Author Administrator
 * @Description //TODO
 * @Date 2018/12/15/015
 **/

import java.util.UUID;

public class ProductQueueTest {
    public static void main(String[] args) {
        ProductQueue<String> stringProductQueue = new ProductQueue<>();
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        stringProductQueue.put(UUID.randomUUID().toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        stringProductQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }
}
