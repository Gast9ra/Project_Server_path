package com.collaboration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class MonoThreadClientHandler implements Runnable {

	private static Socket clientDialog;

	private static DataInputStream inStream;
	private static DataOutputStream outStream;


	public MonoThreadClientHandler(Socket client) {
		MonoThreadClientHandler.clientDialog = client;
	}





	@Override
	public void run() {

		try {

			inStream = new DataInputStream(clientDialog.getInputStream());
			outStream = new DataOutputStream(clientDialog.getOutputStream());

			if (!authentication(inStream.readUTF())) clientDialog.close(); //check data need json

			while (!clientDialog.isClosed()) {
				String entry="";
				System.out.println(clientDialog.isInputShutdown());
				inStream.readUTF();
			//	String entry = inStream.readUTF();


				switch (entry){
					case "create":
						System.out.print("create");
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
					outStream.writeUTF("Server reply - " + entry + " - OK");
					Thread.sleep(3000);
					break;
				}




				System.out.println("Server try writing to channel");
				outStream.writeUTF("Server reply - " + entry + " - OK");
				System.out.println("Server Wrote message to clientDialog.");


				outStream.flush();


			}


			inStream.close();
			outStream.close();

			clientDialog.close();



		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	/**
		* takes json and send to sql. if user haven't in database send
	 	* request to user need register or not
	 */
	private static boolean authentication(String login){
		try {
			outStream.writeUTF("true");
			outStream.flush();
			System.out.println("go");
			// TODO: 12.03.2018
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}



	/**
	 * check data and if true create project in sql
	 */
	public static void createProject(String json){
		// TODO: 12.03.2018
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
