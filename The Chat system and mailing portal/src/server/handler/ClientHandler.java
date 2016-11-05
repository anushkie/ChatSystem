package server.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import server.client.User;
import server.service.LoginService;

/**
 * 
 * @author Anushka
 *
 */
public class ClientHandler implements Runnable {
	
	private DataInputStream dis;
	
	private DataOutputStream dos;
	
	private Socket socket;
	
	private LoginService loginService;
	
	private int counter;
	//map is an interface concurrentHashMAps etc are implementations
	private Map<Socket, User> connectedUsers;
	
	private boolean isSocketConnected;
	
	private String userName;
	
	public ClientHandler(Socket socket, Map<Socket, User> map, int counter) {
		this.socket = socket;
		this.connectedUsers = map;
		this.counter = counter;
		this.isSocketConnected = true;
		loginService = new LoginService();
		
		try {
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			isSocketConnected = false;
		}
	}
	
	@Override
	public void run() {

		//System.out.println("A thread has started running");
		while(isSocketConnected) {
			
			try {
				
				String message = dis.readUTF();
				
				if(message.charAt(0) == 'L') {
					System.out.println("It is a lOGIN message");
					String userName;
					String password;
					String[] splitLoginString = message.split("-");
					userName = splitLoginString[0];
					password = splitLoginString[1];
					boolean isFailure = false;
					
					this.userName = userName.substring(1);
					if(loginService.isValidUser(this.userName, password)) {
						
						for(Map.Entry<Socket, User> entry : connectedUsers.entrySet()) {
							
							if(userName.substring(1).equals(entry.getValue().getUserName())) {
								System.out.println("sending failure message to " + entry.getValue().getUserName());
								isFailure = true;
								break;
							}
						}
						
						if(isFailure) {
							dos.writeUTF("FL"); 
							continue;
						} else {
							dos.writeUTF("SL");

							dos.flush();
							User user = new User(userName.substring(1), password, dis, dos);
							connectedUsers.put(socket, user);
							
						}
							
					} else {
						dos.writeUTF("FL"); 
						continue;
						
					}
										
				} else {
					if(message.charAt(0) == 'C') {
						System.out.println("This is a CHAT message");
						for(Map.Entry<Socket, User> entry : connectedUsers.entrySet()) {
							if(entry.getKey() != socket) {
								User user = entry.getValue();
								user.getDos().writeUTF(userName + ":" + message.substring(1));
							}
						}
						System.out.println("message : " + message.substring(1));

					} else {
						if(message.charAt(0) == 'R') {
							System.out.println("This is a REGISTER function");

							String[] splitRegisterString = message.split("-");
							String firstName = splitRegisterString[0].substring(1);
							String lastName = splitRegisterString[1];
							String username = splitRegisterString[2];
							String password = splitRegisterString[3];
							String emailId = splitRegisterString[4];
							String gender = splitRegisterString[5];
						
							System.out.println("FIRST NAME : " + firstName);
							System.out.println("LAST NAME : " + lastName);
							System.out.println("USERNAME : " + username);
							System.out.println("PASSWORD :" + password);
							System.out.println("EMAIL ID :" + emailId);
							System.out.println("GENDER : " + gender);
							
							if(loginService.doesUserNameExists(username)) {
								dos.writeUTF("FRU");
							} else {
								if(loginService.doesEmailExists(emailId)) {
									dos.writeUTF("FRE");
								} else {
									loginService.registerUser(firstName, lastName, username, password, emailId, gender);
									dos.writeUTF("SR");
									dos.flush();
								}
							}
							
						} else {
							System.out.println("Function Undetected");
						}
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("This client is disconnected");
				connectedUsers.remove(socket);
				isSocketConnected = false;
			}
		}
	}
}
