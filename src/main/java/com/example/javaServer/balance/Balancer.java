package com.example.javaServer.balance;/*
 * @Author Administrator
 * @Description //TODO
 * @Date 2018/12/21/021
 **/

import org.zeromq.ZMQ;

public class Balancer {
    private volatile  int totalHandled = 0;

    public class WorkerThread extends Thread {
        private ZMQ.Context ctx;
        private int handled = 0;
        private int threadNo = 0;

        public WorkerThread(int threadNo, ZMQ.Context ctx) {
            super("Worker-" + threadNo);
            this.threadNo = threadNo;
            this.ctx = ctx;
        }

        public void run(){
            try {
                //Create Pull Socket
                ZMQ.Socket socket = ctx.socket(ZMQ.PULL);
                //Set high water mark to 2,
                // so that when this peer
                // had 2 messages in its buffer
                //ZeroMQ skipped to next workers
                socket.setHWM(2);

                //Connect to in-process endpoint
                socket.connect("inproc://workers");

                while (true) {
                    byte[] msg;
                    try {
                        //Get work piece
                        msg = socket.recv(0);
                    } catch (Exception e) {
                        //ZeroMQ throws exception
                        //when context is terminated
                        socket.close();
                        break;
                    }
                    handled++;
                    totalHandled++;
                    System.out.println(getName() +
                            " handled work piece " + msg[0]);
                    int sleepTime = (threadNo % 2 == 0) ? 100 : 200;
                    //Handle work, by sleeping for some time
                    Thread.sleep(sleepTime);

                }
                System.out.println(getName()
                        + " handled count " + handled);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            //Create ZeroMQ context
            ZMQ.Context ctx = ZMQ.context(1);
            //Create PUSH socket
            ZMQ.Socket socket = ctx.socket(ZMQ.PUSH);
            //Bind socket to in-process endpoint
            socket.bind("inproc://workers");

            //Create worker threads pool
            Thread threads[] = new Thread[10];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new WorkerThread(i, ctx);
                threads[i].start();
            }

            //"send " work to workers
            for (int i = 0; i < 100; i++) {
                System.out.println("sending work piece " + i);
                byte[] msg = new byte[1];
                msg[0] = (byte) i;
                socket.send(msg, 0);
            }
            socket.close();
            Thread.sleep(10000);

            //Terminate ZeroMQ context
            ctx.term();
            System.out.println("Total handled " + totalHandled);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Balancer balancer = new Balancer();
        balancer.run();
    }

}
