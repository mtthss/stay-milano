package com.staymilano.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Itinerary implements Serializable{

	private static final long serialVersionUID = -9024000010811787150L;
	
	private Integer id;
	private Calendar date;
	private List<PointOfInterest> selectedPois=new ArrayList<PointOfInterest>();
	private StartingPoint start;
	
	public Itinerary(String id,String date){
		this.id=Integer.parseInt(id);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			cal.setTime(sdf.parse(date));
			this.date=cal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Itinerary() {
	}

	public Calendar getDate() {
		return date;
	}

	public void setData(Calendar date) {
		this.date=date;
	}	
	
	public void setPois(List<PointOfInterest> pois){
		for(PointOfInterest poi:pois){
			selectedPois.add(poi);
		}
		
	}
	
	public List<PointOfInterest> getPois(){
		return selectedPois;
	}

	public String getID() {
		return String.valueOf(this.id);
	}

	public void setID(String id) {
		this.id = Integer.parseInt(id);
	}

}
