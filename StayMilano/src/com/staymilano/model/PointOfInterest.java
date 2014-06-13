package com.staymilano.model;

import java.io.Serializable;

import android.database.Cursor;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class PointOfInterest implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6297551948085356909L;
	static final String MONUMENT="monument";
	static final String MUSEUM="museum";
	static final String CHURCH="curch";
	static final String THEATRE="theater";
	static final String ARCHEOLOGICAL_SITE="archeological site";
	
	private String id;
	private String name;
	private String type;
	private String description;
	private LatLng position;
	private Bitmap icon;
	
	public PointOfInterest(Cursor cur) {
		this.id=cur.getString(0);
		this.name=cur.getString(1);
		this.description=cur.getString(2);
		setType(cur.getString(3));
		setPosition(cur.getString(5),cur.getString(6));
	}

	public PointOfInterest() {
	}

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
		}else if(type.equalsIgnoreCase(CHURCH)){
			this.type=CHURCH;
		}else{
			this.type=ARCHEOLOGICAL_SITE;
		}
	}
	
	public void setPosition(String lat,String lng){
		position=new LatLng(Float.parseFloat(lat), Float.parseFloat(lng));
	}
	
	public LatLng getPosition() {
		return position;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
