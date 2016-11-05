package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 
 * @author Anushka
 *
 */
public class Client {
	
	private String ipAddress;
	
	private int portNo;
	
	//a constructor to initialize the port number and ip address
	public Client(String ipAddress, int portNo ) {
		this.ipAddress = ipAddress;
		this.portNo = portNo;
	}
	
	public void connect() throws UnknownHostException, IOException, InterruptedException {
		/*
		 * The constructor of the Socket class attempts to connect the client to the specified server and port number. 
		 * If communication is established, the client now has a Socket object capable of communicating with the server.
		 */
		Socket socket = new Socket(ipAddress, portNo);
		//data IO streams to read and write data
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		/*
		 * client's display screen
		 */
		char wish = 0;
		do  {
			System.out.println("WELCOME");
			System.out.println("1. LOGIN");
			System.out.println("2. REGISTER");
			System.out.println("3. EXIT:  ");
			System.out.println("ENTER YOUR CHOICE (1/2/3) : ");
			
			Scanner scanner = new Scanner(System.in);
			int choice = scanner.nextInt();
			scanner.nextLine();
			
			if(choice == 1) {
				System.out.println("ENTER USERNAME :  ");
				String userName = scanner.nextLine();
				System.out.println("ENTER PASSWORD :  " );
				String password = scanner.nextLine();
				
				dos.writeUTF("L" + userName + "-" + password);
		
				
				String messageToPrint = dis.readUTF();
				
				if(messageToPrint.equals("SL")) {
					System.out.println("Login Successful!");
					System.out.println("You can now start chatting..");
					
					MessageHandler messageHandler = new MessageHandler(dis);
					Thread thread = new Thread(messageHandler);
					thread.start();
					
					while(true) {
						String chatMessage = scanner.nextLine();
						if(chatMessage.equalsIgnoreCase("quit")) {
							break;
						}
						dos.writeUTF("C" + chatMessage);
					}
				}
				else {
					System.out.println("Login Failed. Incorrect Username / password!");
					String failureMessage = dis.readUTF();
					System.out.println("this is running");
					if(failureMessage.equals("FL")) {
						System.out.println("Username already exists");
					}
				}
			
			} else { 
				if(choice == 2) {
					System.out.println("ENTER FIRST NAME :  " );
					String firstName = scanner.next();
					System.out.println("ENTER LAST NAME :  ");
					String lastName = scanner.next();
					System.out.println("ENTER USERNAME :  " );
					String userName = scanner.next();
					System.out.println("ENTER PASSWORD :  ");
					String password = scanner.next();
					System.out.println("CONFIRM PASSWORD:  ");
					password = scanner.next();
					System.out.println("ENTER EMAIL_ID:  ");
					String emailId = scanner.next();
					System.out.println("ENTER YOUR GENDER(Male/Female) : ");
					String  gender = scanner.next();
					dos.writeUTF("R" + firstName + "-" + lastName + "-" + userName + "-" + password + "-" + emailId + "-" + gender);
					dos.flush();
										
					String messageToPrint = dis.readUTF();
					
					if(messageToPrint.equals("SR")) {
						System.out.println("Registered Successful!");
						System.out.println("Do you want to continue ? (Y/N) ");
						String str = scanner.next();
						wish = str.charAt(0);
					} else {
						if(messageToPrint.equals("FRE")) {		
							System.out.println("Registration Unsuccessful, Username already exists");
						} else {
							System.out.println("An account already exists on this Email Id");
						}
					}	
				} 
			} 
		}while(wish == 'Y' || wish == 'y');
		socket.close();		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Client client = new Client("localHost", 81);
		client.connect();		
	}
}
