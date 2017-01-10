package com.usermanger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class UserModel{
	
	private String id;
	private String name;
	private String email;
	private String dateOfCreation;
	private String password;
	public UserModel(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}
	public UserModel() {
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@JsonIgnore
	public String getId() {
		return id;
	}
	@JsonIgnore
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonIgnore
	public String getDateOfCreation() {
		return dateOfCreation;
	}
	
	@JsonIgnore
	public void setDateOfCreation(String dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
