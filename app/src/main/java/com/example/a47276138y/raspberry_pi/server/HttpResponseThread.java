package com.example.a47276138y.raspberry_pi.server;

import android.app.Activity;

import com.example.a47276138y.raspberry_pi.Bin;
import com.example.a47276138y.raspberry_pi.MainActivity;
import com.example.a47276138y.raspberry_pi.ManagerDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by 47276138y on 04/04/17.
 */

public class HttpResponseThread extends Thread {

    Socket socket;
    String msgLog = "";
    Activity main_activity;

    HttpResponseThread(Socket socket, Activity MainAct){
        this.socket = socket;
        this.main_activity = MainAct;
    }

    @Override
    public void run() {
        BufferedReader is;
        PrintWriter os;
        String request;


        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            request = is.readLine();

            os = new PrintWriter(socket.getOutputStream(), true);

            String headerLine = request;
            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            String httpQueryString = tokenizer.nextToken();

            System.out.println("httpMethod -> "+httpMethod);
            System.out.println("httpQueryString -> "+httpQueryString);

            switch(httpMethod){
                case "GET":
                        handleRequest_GET(httpQueryString, os);
                    break;

                case "POST":
                        handleRequest_POST(httpQueryString, os);
                    break;
                default:
                    handleRequest_GET("/404", os);
            }

            socket.close();


            msgLog += "Request of " + request
                    + " from " + socket.getInetAddress().toString() + "\n";

            main_activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    System.out.println(msgLog);
                    //infoMsg.setText(msgLog);
                }
            });

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return;
    }

    private void handleRequest_POST(String httpQueryString, PrintWriter os) {
        Bin bin = ((MainActivity) main_activity).getBin();
        ManagerDB managerDB = ((MainActivity) main_activity).managerDB;
        switch(httpQueryString){
            case "/updateBin":
                managerDB.updateBin();
                os.print("HTTP/1.0 302" + "\r\n");
                os.print("Location: /" + "\r\n");
                break;

            case "/pushMeteo":
                    bin.getSensors();
                    managerDB.pushMeteo();
                os.print("HTTP/1.0 302" + "\r\n");
                os.print("Location: /" + "\r\n");
                break;

            default:
                os.print("Location: /404" + "\r\n");
                break;
        }
        os.flush();

    }

    private void handleRequest_GET(String httpQueryString, PrintWriter os) {
        String response;
        Bin bin = ((MainActivity) main_activity).getBin();
        switch(httpQueryString){
            case "/":
                os.print("HTTP/1.0 200" + "\r\n");
                response =
                        "<html><head></head>" +
                                "<body>" +
                                "<h1>" + "Dashboard SmartBin" + "</h1>" +
                                "<hr>" +
                                "<ul>" +
                                "<li>"+"Nombre paperera: "+bin.get_name()+"</li>"+
                                "<li>"+"Peso: "+bin.getCurrWeight()+"/"+bin.getTotalWeight()+"</li>"+
                                "<li>"+"Localització: "+bin.getLocation()+"</li>"+
                                "</ul>"+
                                "<form method='POST' action='/updateBin'>"+
                                    "<input type='submit' name='submit' value='updateBin'>"+
                                "</form>"+
                                "<form method='POST' action='/pushMeteo'>"+
                                "<input type='submit' name='submit' value='pushMeteo'>"+
                                "</form>"+
                                "</body></html>";
                break;

            default:
                os.print("HTTP/1.0 404" + "\r\n");
                response =
                        "<html><head></head>" +
                                "<body>" +
                                "<h1>404 Error</h1>" +
                                "</body></html>";
                break;
        }

        os.print("Content type: text/html" + "\r\n");
        os.print("Content length: " + response.length() + "\r\n");
        os.print("\r\n");
        os.print(response + "\r\n");
        os.flush();

    }
}
