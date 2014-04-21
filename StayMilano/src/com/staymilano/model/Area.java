package com.staymilano.model;

import java.util.ArrayList;
import java.util.List;

public class Area {

	private String name;
	private List<PointOfInterest> pois;

	public Area(AreasName nm) {
		this.name=nm.toString();
		this.pois=fillPois(nm);
	}
	
	private List<PointOfInterest> fillPois(AreasName nm) {
		pois=new ArrayList<PointOfInterest>();
		//TODO caricamento dei dati dal file dei punti d'interesse
		return null;
	}

	public String getName() {
		return name;
	}

	public List<PointOfInterest> getPois() {
		return pois;
	}

}
