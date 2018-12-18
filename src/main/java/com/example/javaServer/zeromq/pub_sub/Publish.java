package com.example.javaServer.zeromq.pub_sub;/*
 * @Author Administrator
 * @Description 消息发布
 * @Date 2018/12/18/018
 **/

import org.zeromq.ZMQ;

public class Publish {

    public static void main(String[] args) {
        //Prepare our context and publisher

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);

        publisher.bind("tcp://*:5563");
        while (!Thread.currentThread().isInterrupted()) {
            //Write two messages, each with an envelope and content
            publisher.sendMore("A");  //订阅topic
            publisher.send("We don't want to see this");
            publisher.sendMore("B");
            publisher.send("We would like to see this");

        }
        publisher.close();
        context.term();

    }
}
