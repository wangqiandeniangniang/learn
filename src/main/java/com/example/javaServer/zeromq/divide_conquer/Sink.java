package com.example.javaServer.zeromq.divide_conquer;/*
 * @Author Administrator
 * @Description 汇总数据
 * @Date 2018/12/18/018
 **/


import org.zeromq.ZMQ;

public class Sink {
    public static void main(String[] args) {
        //prepare our context and socket

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket receiver = context.socket(ZMQ.PULL);
        receiver.bind("tcp://*:5558");
        //Wait for start of batch

        String string = new String(receiver.recv(0));

        //Start our clock now
        long tstart = System.currentTimeMillis();

        //Process 100 confirmations

        int task_nbr;
        int total_msec = 0; // Total calculated cost in msecs
        for (task_nbr = 0; task_nbr < 100; task_nbr++) {
             string = new String(receiver.recv(0)).trim();
            if ((task_nbr / 10) * 10 == task_nbr) {
                System.out.print(":");
            } else {
                System.out.print("_");
            }
        }

        //Calculate and report duration of batch
        long tend = System.currentTimeMillis();
        System.out.println("\nTotal elapsed time : " + (tend - tstart) + " msec");
        receiver.close();
        context.term();

    }
}
