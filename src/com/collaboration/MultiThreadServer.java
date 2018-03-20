package com.collaboration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Driver;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MultiThreadServer {

	private static ExecutorService executeIt = Executors.newFixedThreadPool(2);
	private static final String URL = "jdbc:mysql://192.168.0.37:3306/collaboration?autoReconnect=true&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection connection;


		try (ServerSocket server = new ServerSocket(15233)) {
			System.out.println("Server socket created, command console reader for listen to server commands");

			Driver driver ;


			while (!server.isClosed()) {			


				Socket client = server.accept();

				executeIt.execute(new MonoThreadClientHandler(client));	//main code server
				System.out.print("Connection accepted.");
			}

			executeIt.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}