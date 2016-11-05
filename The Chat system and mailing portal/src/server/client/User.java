package server.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * 
 * @author Anushka
 *
 */
public class User {
	
	private String userName;
	
	private String password;
	
	private DataInputStream dis;
	
	private DataOutputStream dos;
	
	public User(String userName, String password, DataInputStream dis, DataOutputStream dos) {
		this.userName = userName;
		this.password = password;
		this.dis = dis;
		this.dos = dos;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}
}