package com.staymilano.model;

import java.util.Date;
import java.util.List;

public class Itinerary {

	private Date date;
	private List<PointOfInterest> selectedPois;
	private List<VisitedPOI> visitedPois;
	private StartingPoint start;
	
	public boolean isCompleted(){
		if(selectedPois.size()==visitedPois.size()){
			return true;
		}else{
			return false;
		}
	}
	
	public void addToVisited(PointOfInterest poi){
		VisitedPOI vPoi=new VisitedPOI(poi);
		visitedPois.add(vPoi);
	}
}
