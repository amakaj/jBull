package main.java;

public class User {

	String firstName, lastName, username, password, email;
	public User(String fn, String ln, String u, String pw, String e)
	{
		this.firstName = fn;
		this.lastName = ln;
		this.username = u;
		this.password = pw;
		this.email = e;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
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
}