package com.staymilano.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.database.sqlite.SQLiteDatabase;

public class City implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1875465241467662415L;

	private static String MILANO="Milano";
	
	private String name;
	private List<Area> areas;
	
	private static City city;

	private City(SQLiteDatabase db) {
		this.areas=new ArrayList<Area>();
		fillAreas(db);
	}
	
	public static City getCity(SQLiteDatabase db){
		if(city==null){
			city=new City(db);
		}
		return city;
	}

	private List<Area> fillAreas(SQLiteDatabase db) {
		for(AreasName nm: AreasName.values()){
			Area area=new Area(db, nm);
			areas.add(area);
		}
		return areas;
	}
	
	public List<PointOfInterest> getPOIbyArea(String area){
		for(Area a : areas){
			if(a.getName().equalsIgnoreCase(area)){
				return a.getPois();
			}
		}
		return null;
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<Area> getAllAreas(){
		return areas;
	}
	
	public List<PolygonOptions> getPolygons(){
		List<PolygonOptions> pols=new ArrayList<PolygonOptions>();
		for(Area a:areas){
			pols.add(a.getPolygon());
		}
		return pols;
	}
		
}
