package com.staymilano.model;

import java.io.Serializable;

import android.database.Cursor;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.staymilano.R;

public class PointOfInterest implements Serializable{
	
	private static final long serialVersionUID = -6297551948085356909L;
	static final String MONUMENT="monument";
	static final String CHURCH="church";
	static final String PARK="park";
	static final String STATION="station";
	static final String ARCHEOLOGICAL_SITE="archeological_site";
	static final String SHOPPING="shopping";
	static final String SKYSCRAPERS="skyscrapers";
	
	private String id;
	private String name;
	private String type;
	private String description;
	private LatLng position;
	private int icon;
	
	
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
			this.setIcon(R.drawable.museumyellow);
		}else if(type.equalsIgnoreCase(PARK)){
			this.type=PARK;
			this.setIcon(R.drawable.parkgreen);
		}else if(type.equalsIgnoreCase(CHURCH)){
			this.type=CHURCH;
			this.setIcon(R.drawable.churchorange);
		}else if(type.equalsIgnoreCase(STATION)){
			this.type=STATION;
			this.setIcon(R.drawable.stazionered);
		}else if(type.equalsIgnoreCase(SHOPPING)){
			this.type=SHOPPING;
			this.setIcon(R.drawable.bagpink);
		}else if(type.equalsIgnoreCase(SKYSCRAPERS)){
			this.type=SKYSCRAPERS;
			this.setIcon(R.drawable.grattacielilightblue);
		}else{
			this.type=ARCHEOLOGICAL_SITE;
			this.setIcon(R.drawable.museumyellow);
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

	public int getIcon() {
		return icon;
	}
	
	private void setIcon(int iconid) {
		this.icon=iconid;
	}

	
}
