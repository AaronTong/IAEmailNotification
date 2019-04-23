package com.asl.ia.notification.bean;

import org.springframework.stereotype.Component;

@Component
public class Smtp {
	private String ip;
	private String user;
	private String password;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
