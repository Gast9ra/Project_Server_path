package com.collaboration.Trash;

import com.collaboration.ClientForAndroid;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



class Go extends Thread{

    @Override
    public  void run() {
        for(int i=0;i<6;i++){
            System.out.println("1");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class TheardMain {

    public static void main(String[] args) throws IOException {


    }

//    public static void main(String[] args) throws InterruptedException {
//        Go test=new Go();
//        test.start();
//        for(int i=0;i<6;i++){
//            System.out.println("2");
//            System.out.println(test.isAlive());
//            Thread.sleep(1);
//        }
//    }

}
