package com.staymilano.model;

import com.google.android.gms.maps.model.LatLng;

public class PointOfInterest {
	
	private static final String MONUMENT="monument";
	private static final String MUSEUM="museum";
	
	private String name;
	private String type;
	private String description;
	private LatLng position;
	
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
	
	public void setPosition(String lat,String lng){
		position=new LatLng(Float.parseFloat(lat), Float.parseFloat(lng));
	}
	
	public LatLng getPosition() {
		return position;
	}
}
