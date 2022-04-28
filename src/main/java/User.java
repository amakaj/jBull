package main.java;

public class User {

	String username, password, email, phone;
	User(String u, String pw, String e, String p){
		this.username = u;
		this.password = pw;
		this.email = e;
		this.phone = p;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone;
	}
}
