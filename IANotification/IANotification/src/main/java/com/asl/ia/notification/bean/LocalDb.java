package com.asl.ia.notification.bean;

import org.springframework.stereotype.Component;

@Component
public class LocalDb {
	private String path;
	private String dbName;

	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return "{LocalDb: path: "+getPath()+"}";
	}
}
