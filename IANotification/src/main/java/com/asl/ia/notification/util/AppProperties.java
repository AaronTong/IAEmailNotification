package com.asl.ia.notification.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.asl.ia.notification.bean.Documentum;
import com.asl.ia.notification.bean.LocalDb;
import com.asl.ia.notification.bean.Smtp;
import com.asl.ia.notification.query.DqlQuery;

@Component
@ConfigurationProperties(prefix="notification")
public class AppProperties {
	
	@Autowired
	private LocalDb localDb;
	@Autowired
	private Smtp smtp;
	@Autowired
	private Documentum documentum;
	public LocalDb getLocalDb() {
		return localDb;
	}
	public void setLocalDb(LocalDb localDb) {
		this.localDb = localDb;
	}
	public Smtp getSmtp() {
		return smtp;
	}
	public void setSmtp(Smtp smtp) {
		this.smtp = smtp;
	}
	public Documentum getDocumentum() {
		return documentum;
	}
	public void setDocumentum(Documentum documentum) {
		this.documentum = documentum;
	}
	
	@Override
	public String toString() {
		return localDb.toString();
	}
}
