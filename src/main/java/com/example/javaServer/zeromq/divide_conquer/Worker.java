package com.example.javaServer.zeromq.divide_conquer;/*
 * @Author Administrator
 * @Description  将事情发给sink
 * @Date 2018/12/18/018
 **/

import org.zeromq.ZMQ;

public class Worker {
    public static void main(String[] args) throws InterruptedException {
        ZMQ.Context context = ZMQ.context(1);

        //Socket to receive messages on

        ZMQ.Socket receiver = context.socket(ZMQ.PULL);
        receiver.connect("tcp://localhost:5557");

        //Socket to send messages to
        ZMQ.Socket sender = context.socket(ZMQ.PUSH);
        sender.connect("tcp://localhost:5558");

        //Process tasks forever
        while (!Thread.currentThread().isInterrupted()) {
            String string = new String(receiver.recv(0)).trim();
            long msec = Long.parseLong(string);
            //Simple progress indicator for the viewer
            System.out.flush();
            System.out.print(string + "_");

            //Do the work
            Thread.sleep(msec);
            //Send results to sink
            sender.send("".getBytes(), 0);
        }
        sender.close();
        receiver.close();
        context.term();

    }
}
