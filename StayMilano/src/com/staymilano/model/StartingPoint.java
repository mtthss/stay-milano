package com.staymilano.model;

import com.google.android.gms.maps.model.LatLng;

public class StartingPoint implements PointOfItinerary{
	
	public static String STARTING_POINT="starting_point";
	
	public String name;
	public String type=STARTING_POINT;
	public LatLng position;

	@Override
	public String getName() {
		if(name==null){
			name=STARTING_POINT;
		}
		return this.name;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public LatLng getPosition() {
		return this.position;
	}
	
	public void setPosition(LatLng coord){
		this.position=coord;
	}
}
