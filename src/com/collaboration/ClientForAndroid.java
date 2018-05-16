package com.collaboration;


import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * добавить wait() для того чтобы не разрывать соединеение с клиентом тогда в каждом методе нужно
 * добавить notify() ,но надо посмотреть можно ли вызвать для определеного потока
 */


public class ClientForAndroid implements Runnable {
    private String mServerMessage;
    private boolean mRun = false; // флаг, определяющий, запущен ли сервер
    private PrintWriter mBufferOut;
    private BufferedReader mBufferIn;
    private Socket socket;
    private String address;

    public ClientForAndroid(Socket server) throws InterruptedException {
        this.socket=server;
        Thread thread = new Thread(this, "Net");
        System.out.println("check" + thread);
        thread.start();
        Thread.sleep(20);
    }

    public void sendMessage(String message) throws InterruptedException {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
        Thread.sleep(20);
    }

    public void stopClient() throws IOException, InterruptedException {
        sendMessage("Client is close");

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }


    @Override
    public void run() {
        try {
            System.out.println("Connect to server");
            try {
//                socket = new Socket("localhost", 15233);
                mRun = true;
                mBufferOut =
                        new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
                                true);
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // ждем ответа
                while (mRun) {
                    if (mBufferOut.checkError()) {
                        mRun = false;
                    }

                    mServerMessage = mBufferIn.readLine();
                    if(mServerMessage.equalsIgnoreCase("quit")){
                       sendMessage("quit");
                       socket.close();
                    }

                }
            } catch (Exception ignored) {
            } finally {
                if (socket != null && socket.isConnected()) {
                    socket.close();
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public boolean isRunning() {
        return mRun;
    }

    public String getmServerMessage() throws InterruptedException {
        Thread.sleep(20);
        return mServerMessage;
    }



    //в приложение

    /**
     * получение номера проекта на запрос на присоединении
     * @param id
     */

    public void joinProject(int id){
        JSONObject json  = new JSONObject();
        json.put("JsonMessaged", "command");
        json.put("command", "joinProject");
        json.put("id" , id);
    }


    /**
     * получение определеного проекта по id
     * возращиние конструктора проекта
     * @param id
     * @return
     */
    public AndroidProjects getProject(int id){
            AndroidProjects result = new AndroidProjects();
            return result;
    }


    /**
     * возвращение тоже самое из поиска только без начальных данных
     */
    public ArrayList<AndroidProjects> listProject(){
        JSONObject jsonout = null;
        ArrayList<AndroidProjects> result=new ArrayList<>();
        jsonout.put("command", "listProject");
        return result;
    }

    /**
        возвращение листа кострукторов проэктов
     */
    public AndroidProjects searchProject(String name){
        int id=0;
        int idLeader=0;
        ArrayList<Users> listUsers=new ArrayList<>();
        JSONObject json  = new JSONObject();
        json.put("name", name);
        json.put("command", "searchProjects");
        AndroidProjects result=new AndroidProjects(id,name,idLeader,listUsers);
        return result;
    }



    public static void createProject( String name,String discript,int numPeople){
        JSONObject json  = new JSONObject();
        json.put("JsonMessaged", "command");
        json.put("command", "create");
        JSONArray ar = new JSONArray();
        JSONObject jsonAr  = new JSONObject();
        jsonAr.put("name", name);
        jsonAr.put("text", discript);
        jsonAr.put("num", numPeople);
        ar.add(jsonAr);
        json.put("data", ar);
    }

    public User searchUser(String name){
        JSONObject json  = new JSONObject();
        json.put("name", name);
        json.put("command", "searchUser");
        User result = new User();
        return result;
    }

}