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
    private int idUser=1;

    public ClientForAndroid(Socket server) throws InterruptedException {
        this.socket = server;
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
                    if (mServerMessage.equalsIgnoreCase("quit")) {
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
     *
     * @param id
     */

    public void joinProject(int id) {
        JSONObject json = new JSONObject();
        json.put("JsonMessaged", "command");
        json.put("command", "joinProject");
        json.put("id", id);
    }


    /**
     * получение определеного проекта по id
     * возращиние конструктора проекта
     *
     * @param id
     * @return
     */
    public AndroidProjects getProject(int id) throws InterruptedException, ParseException {
        ArrayList<Users> listUsers = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("command", "getProjects");
        sendMessage(json.toString());
        Thread.sleep(200);

        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(getmServerMessage());
        JSONObject js = (JSONObject) jsob;
        js.get("user"); // поместить json массив в listUser

        AndroidProjects result = new AndroidProjects(id, (String) js.get("name"), (Integer) js.get("leader"), listUsers);
        return result;
    }


    /**
     * возвращение тоже самое из поиска только без начальных данных
     */
    public ArrayList<AndroidProjects> listProject() {
        JSONObject jsonout = null;
        ArrayList<AndroidProjects> result = new ArrayList<>();
        jsonout.put("command", "listProject");
        return result;
    }

    /**
     * возвращение листа кострукторов проэктов
     */
    public AndroidProjects searchProject(String name) throws InterruptedException, ParseException {
        int id = 0;
        int idLeader = 0;
        ArrayList<Users> listUsers = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("command", "searchProjects");
        sendMessage(json.toString());
        Thread.sleep(200);

        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(getmServerMessage());
        JSONObject js = (JSONObject) jsob;
        js.get("user"); // поместить json массив в listUser

        AndroidProjects result = new AndroidProjects((Integer) js.get("id"), name, (Integer) js.get("leader"), listUsers);
        return result;
    }

    public User searchUser(String name) throws InterruptedException, ParseException {
        int id = 0;
        int idLeader = 0;
        ArrayList<Users> listUsers = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("command", "searchUser");
        sendMessage(json.toString());
        Thread.sleep(200);

        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(getmServerMessage());
        JSONObject js = (JSONObject) jsob;


        User result = new User((Integer) js.get("id")
                , name
                , (String) js.get("leader")
                , (String) js.get("text")
                , (String) js.get("connect"));
        return result;
    }


    public void createProject(String name, String discript, int numPeople) throws InterruptedException {
        JSONObject json = new JSONObject();
        json.put("JsonMessaged", "command");
        json.put("command", "create");
        JSONArray ar = new JSONArray();
        JSONObject jsonAr = new JSONObject();
        json.put("name", name);
        json.put("text", discript);
        json.put("num", numPeople);
        //ar.add(jsonAr);
        //json.put("data", ar);
        sendMessage(json.toString());
    }


}