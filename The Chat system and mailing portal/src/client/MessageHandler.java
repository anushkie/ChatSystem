package client;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 
 * @author Anushka
 *
 */
public class MessageHandler implements Runnable{

	private DataInputStream dis;
	
	public MessageHandler(DataInputStream dis) {
		this.dis = dis;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				String messageToBePrinted = dis.readUTF();
				System.out.println(messageToBePrinted);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
