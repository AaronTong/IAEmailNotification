package com.asl.ia.notification.bean;

import java.util.Set;

import org.springframework.stereotype.Component;


public class Application {
	private int threshold;
	private String name;
	Set<Holding> holdings;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public Set<Holding> getHoldings() {
		return holdings;
	}

	public void setHoldings(Set<Holding> holdings) {
		this.holdings = holdings;
	}
	
}
