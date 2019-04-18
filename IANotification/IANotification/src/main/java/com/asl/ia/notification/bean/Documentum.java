package com.asl.ia.notification.bean;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class Documentum {
	
	Set<Repository> repositories;

	public Set<Repository> getRepositories() {
		return repositories;
	}

	public void setRepositories(Set<Repository> repositories) {
		this.repositories = repositories;
	}
	
}
