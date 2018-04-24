package com.collaboration.Trash;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        try {
            Socket connect = new Socket("localhost", 15233);
            PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(connect.getOutputStream())),
                    true);
            DataInputStream in = new DataInputStream(connect.getInputStream());
            out.println("Massege for test");
            out.flush();
            while (!connect.isOutputShutdown()){
                out.print("spam");
            }

            connect.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
