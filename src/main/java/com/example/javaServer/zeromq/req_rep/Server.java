package com.example.javaServer.zeromq.req_rep;/*
 * @Author Administrator
 * @Description 服务器端
 * @Date 2018/12/18/018
 **/

import org.zeromq.ZMQ;

public class Server {



    public static void main(String[] args) throws InterruptedException {
        ZMQ.Context context = ZMQ.context(1);

        //SOcket to talk to clients

        ZMQ.Socket responder = context.socket(ZMQ.REP);

        responder.bind("tcp://*:5555");
        while (!Thread.currentThread().isInterrupted()) {
            //Wait for next request from the client
            byte[] request = responder.recv(0);
            System.out.println("Received Hello");
            //Do some 'work'

            Thread.sleep(1000);
            //Send reply back to client

            String reply = "World";
            responder.send(reply.getBytes(), 0);
        }
        responder.close();

        context.term();
    }
}
