package com.collaboration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import java.sql.*;

public class MonoThreadClientHandler implements Runnable {

    private static Socket clientDialog;
    private static Connection forSqlConnect;


    private static PrintWriter outStream;
    private static BufferedReader inStream;

    public MonoThreadClientHandler(Socket client, Connection stmt) {
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
                System.out.println(entry);
                JSONParser pars = new JSONParser();
                Object jsob = pars.parse(entry);
                JSONObject js = (JSONObject) jsob;


                switch ((String) js.get("command")) {
                    case "create":
                        System.out.println("Accept Create");
                        createProject(entry);   // put json request

                    case "join":
                        joinProject(entry);   // put json request

                    case "ListProject":
                        listProject(entry); // put json request

                    case "searchProject":
                        searchProject(entry);

                    case "searchUser":
                        searchUser(entry);

                    case "getProjects":
                        getProject(entry);

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
    public static void createProject(String json) throws SQLException, ParseException, InterruptedException {
        //data for the created project
        String projectName;
        String projectLeader = "1";
        String projectText = "";
        //put Json parser here
        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(json);
        JSONObject js = (JSONObject) jsob;

        projectName = (String) js.get("name");
        //projectLeader=(String) js.get("");
        projectText = (String) js.get("text");
        //sql request for
        PreparedStatement request;
        String sqlRequest = "INSERT INTO collaboration.project " +
                "(ProjectName,ProjectLeader,Projecttext) VALUES (?,?,?)";

        request = forSqlConnect.prepareStatement(sqlRequest);
        request.setString(1, projectName);
        request.setString(2, projectLeader);
        request.setString(3, projectText);
        request.execute();
        Thread.sleep(20);
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

        PreparedStatement request;
        request = forSqlConnect.prepareStatement("SELECT ProjectUser FROM project WHERE idProject=?");
        request.setInt(1, idUser);


        ResultSet rs = request.executeQuery();
        while (rs.next()) {
            jsonOnSql = rs.getString(1);
        }
        //add in jsonOnSql user how want to join and check for repeat
        jsob = pars.parse(jsonOnSql);
        js = (JSONObject) jsob;
        JSONArray ar = (JSONArray) js.get("ConnectProj");
        ar.add(idUser);
        js.replace("ConnectProj",ar);


        request = forSqlConnect.prepareStatement("UPDATE project set collaboration.project.ProjectUser=?" +
                " WHERE idProject=?");
        request.setString(1, jsonOnSql);
        request.setInt(2, idUser);
        request.executeUpdate();
    }


    /**
     * send to client list all Project
     */
    private static void listProject(String json) throws SQLException {
        PreparedStatement request;
        request = forSqlConnect.prepareStatement("SELECT ProjectLeader, " +
                "Projecttext, " +
                "ProjectUser " +
                "FROM project ");
        ResultSet rs = request.executeQuery();
        while(rs.next()){

        }


    }


    /**
     * search in database adn send client
     */
    private static void searchProject(String json) throws SQLException, ParseException {
        String nameProject = "";
        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(json);
        JSONObject js = (JSONObject) jsob;
        nameProject = (String) js.get("name");

        PreparedStatement request;
        request = forSqlConnect.prepareStatement("SELECT ProjectLeader, " +
                "Projecttext, " +
                "ProjectUser, idProject " +
                "FROM project " +
                "WHERE ProjectName=?;");
        request.setString(1, nameProject);
        ResultSet rs = request.executeQuery();
        JSONObject jsonOut = new JSONObject();
        while (rs.next()) {
            jsonOut.put("leader", rs.getString(1));
            jsonOut.put("text", rs.getString(2));
            jsonOut.put("user", rs.getString(3));
            jsonOut.put("id", rs.getInt(4));
        }
        outStream.println(jsonOut.toString());
        outStream.flush();

    }

    private static void searchUser(String json) throws ParseException, SQLException {
        String nameUser = "";
        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(json);
        JSONObject js = (JSONObject) jsob;

        PreparedStatement request;
        request = forSqlConnect.prepareStatement("SELECT UserProjectConnect, UserLeader, UserText, idUser " +
                "FROM user WHERE UserName=?;");
        request.setString(1, nameUser);
        ResultSet rs = request.executeQuery();
        JSONObject jsonOut = new JSONObject();
        while (rs.next()) {
            jsonOut.put("connect", rs.getString(1));
            jsonOut.put("leader", rs.getString(2));
            jsonOut.put("text", rs.getString(3));
            jsonOut.put("id", rs.getInt(4));
        }
        outStream.println(jsonOut.toString());
        outStream.flush();
    }

    private static void getProject(String json) throws SQLException, ParseException {
        String nameProject = "";
        JSONParser pars = new JSONParser();
        Object jsob = pars.parse(json);
        JSONObject js = (JSONObject) jsob;
        nameProject = (String) js.get("name");

        PreparedStatement request;
        request = forSqlConnect.prepareStatement("SELECT ProjectLeader, " +
                "Projecttext, " +
                "ProjectUser, ProjectName " +
                "FROM project " +
                "WHERE idProject=?;");
        request.setString(1, nameProject);
        ResultSet rs = request.executeQuery();
        JSONObject jsonOut = new JSONObject();
        while (rs.next()) {
            jsonOut.put("leader", rs.getString(1));
            jsonOut.put("text", rs.getString(2));
            jsonOut.put("user", rs.getString(3));
            jsonOut.put("name", rs.getString(4));
        }
        outStream.println(jsonOut.toString());
        outStream.flush();

    }
}
