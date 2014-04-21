package com.staymilano.model;

import java.util.Date;

public class PointOfInterest {
	
	private String name;
	private String description;

	public PointOfInterest(String nm, String dscp) {
		this.name=nm;
		this.description=dscp;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Date isVisited(){
		return null;
	}

}
