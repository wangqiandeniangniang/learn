package com.example.javaServer.zeromq.req_rep;/*
 * @Author Administrator
 * @Description ZeroMQ 的客户端，关键就是context，获取发送和接收对象
 * @Date 2018/12/18/018
 **/
import org.zeromq.*;
public class Client {

    public static void main(String[] args) {
        ZMQ.Context  context = ZMQ.context(1);
        //Socket to talk to server

        System.out.println("Connecting to hello world server ...");

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5555");
        for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
            String request = "Hello";
            System.out.println("Sending Hello " + requestNbr);
            requester.send(request.getBytes(), 0);

            byte[] reply = requester.recv(0);

            System.out.println("Received " + new String (reply) + " " + requestNbr);
        }
        requester.close();

        context.term();
    }
}
