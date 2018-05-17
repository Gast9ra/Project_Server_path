package com.collaboration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MultiThreadServer {

	private static ExecutorService executeIt = Executors.newFixedThreadPool(2);
	private static final String url = "jdbc:mysql://159.65.119.198:3306/Collaboration"+
			"?verifyServerCertificate=false"+
			"&useSSL=false"+
			"&requireSSL=false"+
			"&useLegacyDatetimeCode=false"+
			"&amp"+
			"&serverTimezone=UTC";
	//private static final String url = "jdbc:mysql://10.210.10.138:3306?autoReconnect=true&useSSL=false";

	private static final String user = "user";
	private static final String password = "collabmysql";


	/**
	 * @param args
	 */
	public static void main(String[] args) {



		try (ServerSocket server = new ServerSocket(15233)) {
			System.out.println("Server socket created, command console reader for listen to Server commands");

			Connection con = DriverManager.getConnection(url, user, password);


			while (!server.isClosed()) {			


				Socket client = server.accept();

				executeIt.execute(new MonoThreadClientHandler(client,con));	  //main code Server

				System.out.println("Connection accepted.");
			}

			executeIt.shutdown();
			con.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}