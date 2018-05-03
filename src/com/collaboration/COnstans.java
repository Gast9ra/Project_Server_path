package com.collaboration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class COnstans {
    public final int port=15233;
    public final String adress="localhost";
    private  static BufferedReader consol = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket=new Socket("localhost",15233);
        ClientForAndroid server=new ClientForAndroid(socket);
        server.sendMessage("Test connect");
        System.out.println(server.getmServerMessage());
        while(!socket.isClosed()) {
            server.sendMessage(consol.readLine());
            System.out.println(server.getmServerMessage());
        }
    }



}
