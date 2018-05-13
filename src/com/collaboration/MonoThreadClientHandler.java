package com.collaboration;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import java.sql.SQLException;
import java.sql.Statement;

public class MonoThreadClientHandler implements Runnable {

	private static Socket clientDialog;
	private static Statement forSqlConnect;


	private static PrintWriter outStream;
	private static BufferedReader inStream;

	public MonoThreadClientHandler(Socket client, Statement stmt) {
		MonoThreadClientHandler.forSqlConnect=stmt;
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

                switch (entry) {
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
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}



	/**
		* takes json and send to sql. if user haven't in database send
	 	* request to user need register or not
	 */
	private static boolean authentication(String login){
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
	    String projectName="";
	    String projectLeader="";
	    String projectText="";
        //put Json parser here


        //sql request for create
		String sqlRequest="INSERT INTO project " +
                "(ProjectName,ProjectLeader,Projecttext)" +
                "value ("+projectName+projectLeader+projectText+")";
		forSqlConnect.executeUpdate(sqlRequest);


	}



	/**
	 * check data and send request to sql for join project
	 */
	private static void joinProject(String json){
		// TODO: 12.03.2018
	}

	/**
	 * send to client list all Project
	 */
	private static void listProject(String json){
		// TODO: 12.03.2018
	}



	/**
	 * search in database adn send client
	 */
	private static void search(String json){
		// TODO: 12.03.2018
	}




}
