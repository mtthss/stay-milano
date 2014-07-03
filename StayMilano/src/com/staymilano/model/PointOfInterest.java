package com.staymilano.model;

import java.io.Serializable;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.staymilano.R;
import com.staymilano.database.PointOfInterestDAO;

public class PointOfInterest implements Serializable,PointOfItinerary{
	
	private static final long serialVersionUID = -6297551948085356909L;
	static final String MONUMENT="monument";
	static final String CHURCH="church";
	static final String PARK="park";
	static final String CASTLE="castle";
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
		this.id=cur.getString(cur.getColumnIndex(PointOfInterestDAO.ID));
		this.name=cur.getString(cur.getColumnIndex(PointOfInterestDAO.NAME));
		this.description=cur.getString(cur.getColumnIndex(PointOfInterestDAO.DESCRIPTION));
		setType(cur.getString(cur.getColumnIndex(PointOfInterestDAO.TYPE)));
		setPosition(cur.getString(cur.getColumnIndex(PointOfInterestDAO.LATITUDE)),
				cur.getString(cur.getColumnIndex(PointOfInterestDAO.LONGITUDE)));
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
			this.setIcon(R.drawable.markermuseumyellowmini);
		}else if(type.equalsIgnoreCase(PARK)){
			this.type=PARK;
			this.setIcon(R.drawable.markerparkgreenmini);
		}else if(type.equalsIgnoreCase(CHURCH)){
			this.type=CHURCH;
			this.setIcon(R.drawable.markerchurchorangemini);
		}else if(type.equalsIgnoreCase(CASTLE)){
			this.type=CASTLE;
			this.setIcon(R.drawable.markercastleblumini);
		}else if(type.equalsIgnoreCase(SHOPPING)){
			this.type=SHOPPING;
			this.setIcon(R.drawable.markerbagpinkmini);
		}else if(type.equalsIgnoreCase(SKYSCRAPERS)){
			this.type=SKYSCRAPERS;
			this.setIcon(R.drawable.markerskylightmini);
		}else{
			this.type=ARCHEOLOGICAL_SITE;
			this.setIcon(R.drawable.markermuseumyellowmini);
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
