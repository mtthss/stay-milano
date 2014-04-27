package com.staymilano.model;

import android.database.sqlite.SQLiteDatabase;

public class PointOfInterest {
	
	private static final String MONUMENT="monument";
	private static final String MUSEUM="museum";
	
	private String name;
	private String type;
	private String description;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		if(type.equalsIgnoreCase(MONUMENT)){
			this.type=MONUMENT;
		}else if(type.equalsIgnoreCase(MUSEUM)){
			this.type=MUSEUM;
		}
	}
}
