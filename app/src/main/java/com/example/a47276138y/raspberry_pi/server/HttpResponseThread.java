package com.example.a47276138y.raspberry_pi.server;

import android.app.Activity;
import android.util.Log;

import com.example.a47276138y.raspberry_pi.Bin;
import com.example.a47276138y.raspberry_pi.MainActivity;
import com.example.a47276138y.raspberry_pi.ManagerDB;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

            String line;
            int contentLength = 0;
            while (!(line = is.readLine()).equals("")) {
                    final String contentHeader = "Content-Length: ";
                    if (line.startsWith(contentHeader)) {
                        contentLength = Integer.parseInt(line.substring(contentHeader.length()));
                    }
            }

            switch(httpMethod){
                case "GET":
                        handleRequest_GET(httpQueryString, os);
                    break;

                case "POST":


                    StringBuilder body = new StringBuilder();
                        int c = 0;
                        for (int i = 0; i < contentLength; i++) {
                            c = is.read();
                            body.append((char) c);
                        }

                        handleRequest_POST(httpQueryString, os, body.toString());
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

    public static Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }

    private void handleRequest_POST(String httpQueryString, PrintWriter os, String params) {
        try {
            Map<String, List<String>> key_params = splitQuery(new URI(params).toURL());
            Bin bin = ((MainActivity) main_activity).getBin();
            ManagerDB managerDB = ((MainActivity) main_activity).managerDB;
            switch (httpQueryString) {
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

                case "/leds":
                    os.print("HTTP/1.0 200" + "\r\n");
                    os.print("Content type: text/html" + "\r\n");
                    os.print("Hola LEDS");
                    if (key_params.containsKey("red")) {
                        System.out.println("red!!");
                        ((MainActivity) main_activity).Leds(RainbowHat.LED_RED, true);
                    } else {
                        ((MainActivity) main_activity).Leds(RainbowHat.LED_RED, false);
                    }
                    if (key_params.containsKey("green")) {
                        System.out.println("green!!");
                        ((MainActivity) main_activity).Leds(RainbowHat.LED_GREEN, true);
                    } else {
                        ((MainActivity) main_activity).Leds(RainbowHat.LED_GREEN, false);
                    }
                    if (key_params.containsKey("blue")) {
                        System.out.println("blue!!");
                        ((MainActivity) main_activity).Leds(RainbowHat.LED_BLUE, true);
                    } else {
                        ((MainActivity) main_activity).Leds(RainbowHat.LED_BLUE, false);
                    }
                    break;

                case "/display":
                    os.print("HTTP/1.0 200" + "\r\n");
                    os.print("Content type: text/html" + "\r\n");
                    os.print("Hola DISPLAY");
                    break;

                default:
                    os.print("Location: /404" + "\r\n");
                    break;
            }
            os.flush();
        }catch(Exception e){

        }
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
                                "<form method='POST' action='/leds'>"+
                                "<input type='checkbox' name='red' value='1' /> RED <br>"+
                                "<input type='checkbox' name='green' value='1' /> GREEN <br>"+
                                "<input type='checkbox' name='blue' value='1' /> BLUE <br>"+
                                "<input type='submit' name='submit' value='submit'>"+
                                "</form>"+
                                "<form method='POST' action='/display'>"+
                                "<input type='text' name='display'>"+
                                "<input type='submit' name='submit' value='submit'>"+
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
