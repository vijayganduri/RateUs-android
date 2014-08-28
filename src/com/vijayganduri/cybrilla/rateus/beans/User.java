package com.vijayganduri.cybrilla.rateus.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.vijayganduri.cybrilla.rateus.dao.UserDao;

@DatabaseTable(tableName = "user", daoClass = UserDao.class)
public class User{

	@DatabaseField(unique = true, id = true)
	private long id;

	@DatabaseField
	private String email;

	@DatabaseField
	private String name;
	
	public User(){
		
	}
	
	public User(long id, String email, String name){
		this.id = id;
		this.email = email;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", name=" + name + "]";
	}

}