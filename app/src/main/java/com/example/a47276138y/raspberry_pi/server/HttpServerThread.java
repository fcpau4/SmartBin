package com.example.a47276138y.raspberry_pi.server;

import android.app.Activity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 47276138y on 04/04/17.
 */






    public class HttpServerThread extends Thread {

        static final int HttpServerPORT = 8888;
        ServerSocket httpServerSocket;
        Activity main_activity;

    public HttpServerThread(ServerSocket httpServerSocket, Activity mainAct) {
        this.main_activity = mainAct;
        this.httpServerSocket = httpServerSocket;
    }

    @Override
        public void run() {
            Socket socket = null;
        System.out.println("HttpServer on port "+HttpServerPORT);
            try {
                httpServerSocket = new ServerSocket(HttpServerPORT);

                while (true) {
                    socket = httpServerSocket.accept();

                    HttpResponseThread httpResponseThread =
                            new HttpResponseThread(
                                    socket, main_activity);
                    httpResponseThread.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }




