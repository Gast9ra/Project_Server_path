package com.collaboration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MonoThreadClientHandler implements Runnable {

    private static Socket clientDialog;
    private static Statement forSqlConnect;


    private static PrintWriter outStream;
    private static BufferedReader inStream;

    public MonoThreadClientHandler(Socket client, Statement stmt) {
        MonoThreadClientHandler.forSqlConnect = stmt;
        MonoThreadClientHandler.clientDialog = client;
    }


    @Override
    public void run() {

        try {

            inStream = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientDialog.getOutputStream()))
                    , true);

//            if (!authentication(inStream.readLine())) clientDialog.close(); //check data need json

            while (!clientDialog.isClosed()) {
                String entry;

                entry = inStream.readLine();
                JSONParser pars = new JSONParser();
                Object jsob = pars.parse(entry);
                JSONObject js = (JSONObject) jsob;


                switch ((String)js.get("command")) {
                    case "create":
                        System.out.println("Accept Create");
                        createProject(entry);   // put json request

                    case "join":
                        joinProject(entry);   // put json request

                    case "need List project":
                        listProject(entry); // put json request

                    case "search":
                        search(entry);

                }

                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client suicide");
                    outStream.println("Server reply - " + entry + " - OK");
                    Thread.sleep(3000);
                    break;
                }


                System.out.println("Server try writing to channel");
                outStream.println("Server reply - " + entry + " - OK");
                System.out.println("Server Wrote message to clientDialog.");


                outStream.flush();


            }


            inStream.close();
            outStream.close();

            clientDialog.close();

        } catch (SocketException e) {
            System.out.println("Client lost connecting");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * takes json and send to sql. if user haven't in database send
     * request to user need register or not
     */
    private static boolean authentication(String login) {
        outStream.println("true");
        outStream.flush();
        // TODO: 12.03.2018
        return true;
    }


    /**
     * check data and if true create project in sql
     */
    public static void createProject(String json) throws SQLException {
        //data for the created project
        String projectName = "";
        String projectLeader = "";
        String projectText = "";
        //put Json parser here


        //sql request for create
        String sqlRequest = "INSERT INTO project " +
                "(ProjectName,ProjectLeader,Projecttext)" +
                "value (" + projectName + projectLeader + projectText + ")";
        forSqlConnect.executeUpdate(sqlRequest);


    }


    /**
     * check data and send request to sql for join project
     * need data id user id project
     * check data not work now
     */
    private static void joinProject(String json) throws SQLException, ParseException {
        int idUser = 0;
        int idProject = 0;
        String jsonOnSql = null;
        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(json);
        JSONObject js = (JSONObject) jsob;
        idUser = (Integer) js.get("id");


        ResultSet rs = forSqlConnect.executeQuery("SELECT ProjectQueue FROM project WHERE idProject=" + idProject);
        while (rs.next()) {
            jsonOnSql = rs.getString(1);
        }
        //add in jsonOnSql user how want to join and check for repeat


        forSqlConnect.executeUpdate("UPDATE project set ProjectQueue="
                + jsonOnSql + "WHERE idProject=" + idProject);
    }

    /**
     * send to client list all Project
     */
    private static void listProject(String json) throws SQLException {

        ResultSet rs = forSqlConnect.executeQuery("SELECT ProjectLeader, " +
                "Projecttext, " +
                "ProjectUser " +
                "FROM project ");

    }


    /**
     * search in database adn send client
     */
    private static void search(String json) throws SQLException, ParseException {
        String nameProject = "";
        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(json);
        JSONObject js = (JSONObject) jsob;
        nameProject = (String) js.get("name");



        ResultSet rs = forSqlConnect.executeQuery("SELECT ProjectLeader, " +
                "Projecttext, " +
                "ProjectUser " +
                "FROM project " +
                "WHERE ProjectName=" + nameProject);


    }


}
