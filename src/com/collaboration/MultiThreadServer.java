package com.collaboration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MultiThreadServer {

	private static ExecutorService executeIt = Executors.newFixedThreadPool(2);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try (ServerSocket server = new ServerSocket(3345)) {
			System.out.println("Server socket created, command console reader for listen to server commands");


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