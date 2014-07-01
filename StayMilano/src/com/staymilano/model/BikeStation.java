package com.staymilano.model;

import com.google.android.gms.maps.model.LatLng;

public class BikeStation implements PointOfItinerary{
	
	public static String BIKE_STATION="bike_station";
	
	private String name;
	private LatLng position;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LatLng getPosition() {
		return position;
	}
	public void setPosition(LatLng position) {
		this.position = position;
	}
	@Override
	public String getType() {
		return BIKE_STATION;
	}

}
