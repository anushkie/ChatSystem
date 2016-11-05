package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import server.client.User;
import server.handler.ClientHandler;

/**
 * 
 * @author Anushka
 *
 */
public class Server {
	
	private boolean isRunnning;
	
	private int portNo;
	
	private int counter;
	
	private ConcurrentHashMap<Socket, User> map;
	
	public Server(int portNo) {
		this.map = new ConcurrentHashMap<>(); 
		this.portNo = portNo;
		this.counter = 0;
	}

	public void start() throws IOException {
		isRunnning = true;
		//The server instantiates a ServerSocket object, denoting which port number communication is to occur on.
		ServerSocket serverSocket = new ServerSocket(portNo);
		//while loop to make the server always available
		while(isRunnning) {
			System.out.println("Waiting for a client to connect..");
			//This method waits until a client connects to the server on the given port.
			//After the server is waiting, a client instantiates a Socket object, specifying the server name and port number to connect to.
			//accept() listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made
			Socket socket = serverSocket.accept(); 
			counter++;
			//the accept() method returns a reference to a new socket on the server that is connected to the client's socket.
			System.out.println("A client connected with remote address : " + socket.getRemoteSocketAddress() + " and Id :" + counter);
			// Thread for different users
			ClientHandler handler = new ClientHandler(socket, map, counter);
			Thread thread = new Thread(handler);
			thread.start();
		}	
		serverSocket.close();
	}
	
	public static void main(String[] args) throws IOException {
		
		Server server = new Server(81);
		server.start();
	}
}