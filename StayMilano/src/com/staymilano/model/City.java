package com.staymilano.model;

import java.util.ArrayList;
import java.util.List;

public class City {
	
	private static String MILANO="Milano";
	
	private String name;
	private List<Area> areas;
	
	private static City city;
	
	private City(){
		this.name=MILANO;
		this.areas=fillAreas();
	}
	
	private List<Area> fillAreas() {
		this.areas=new ArrayList<Area>();
		for(AreasName nm: AreasName.values()){
			Area area=new Area(nm);
			areas.add(area);
		}
		return areas;
	}

	public static City getCity(){
		if(city== null){
			city=new City();
		}
		return city;
	} 

	public List<Area> getAreas() {
		return this.areas;
	}
	
	public String getName(){
		return this.name;
	}
		
}
