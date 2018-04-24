package com.collaboration.Trash;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {



    public static void main(String[] args){
        try {
            ServerSocket server=new ServerSocket(15233);
            Socket client=server.accept();
            System.out.println("connect accept");
            PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),
                    true);
            System.out.println("Stream for output create");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while(!client.isClosed()){
                System.out.println(in.readLine());
                out.println("quit");
                out.flush();
            }

            client.close();



        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }

}
