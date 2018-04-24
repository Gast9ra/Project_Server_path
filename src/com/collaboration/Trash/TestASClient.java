package com.collaboration.Trash;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestASClient {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {


        try (Socket socket = new Socket("localhost", 15233);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream());) {

            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = oos & reading channel = ois initialized.");


            while (!socket.isOutputShutdown()) {


                if (br.ready()) {


                    System.out.println("Client start writing inStream channel...");
                    Thread.sleep(1000);
                    String clientCommand = br.readLine();

                    oos.writeUTF(clientCommand);
                    oos.flush();
                    System.out.println("Clien sent message " + clientCommand + " to Server.");
                    Thread.sleep(1000);


                    if (clientCommand.equalsIgnoreCase("quit")) {


                        System.out.println("Client kill connections");
                        Thread.sleep(2000);


                        if (ois.available() != 0) {
                            System.out.println("reading...");
                            String in = ois.readUTF();
                            System.out.println(in);
                        }


                        break;
                    }


                    System.out.println("Client wrote & start waiting for data from Server...");
                    Thread.sleep(2000);


                    if (ois.available() != 0) {


                        System.out.println("reading...");
                        String in = ois.readUTF();
                        System.out.println(in);
                    }
                }
            }

            System.out.println("Closing connections & channels on clentSide - DONE.");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
