package com.staymilano.model;

import java.util.Date;

public class VisitedPOI extends PointOfInterest {
	
	private Date date;
		
	public VisitedPOI(PointOfInterest poi) {
		super(poi.getName(),poi.getDescription());
		this.date=new Date();
	}

	@Override
	public Date isVisited(){
		return this.date;
	}

}
