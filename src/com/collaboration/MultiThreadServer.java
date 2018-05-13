package com.collaboration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MultiThreadServer {

	private static ExecutorService executeIt = Executors.newFixedThreadPool(2);
	private static final String url = "jdbc:mysql://192.168.0.37:3306/collaboration?autoReconnect=true&useSSL=false";
	private static final String user = "root";
	private static final String password = "root";


	/**
	 * @param args
	 */
	public static void main(String[] args) {



		try (ServerSocket server = new ServerSocket(15233)) {
			System.out.println("Server socket created, command console reader for listen to Server commands");

			Connection con = DriverManager.getConnection(url, user, password);
			Statement stmt = con.createStatement();

			while (!server.isClosed()) {			


				Socket client = server.accept();

				executeIt.execute(new MonoThreadClientHandler(client,stmt));	  //main code Server

				System.out.println("Connection accepted.");
			}

			executeIt.shutdown();
			con.close();
			stmt.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}