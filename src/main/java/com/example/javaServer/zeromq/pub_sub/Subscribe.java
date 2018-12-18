package com.example.javaServer.zeromq.pub_sub;/*
 * @Author Administrator
 * @Description 订阅消息
 * @Date 2018/12/18/018
 **/

import org.zeromq.ZMQ;

public class Subscribe {

    public static void main(String[] args) {
        //Prepare our context and subscriber

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://localhost:5563");
        subscriber.subscribe("B".getBytes());
        while (!Thread.currentThread().isInterrupted()) {
            //Read envelope with address
            String address = subscriber.recvStr(); //获取订阅topic
            //Read message contents
            String contents = subscriber.recvStr();
            System.out.println(address + " : " + contents);
        }
        subscriber.close();
        context.term();
    }
}
