package com.staymilano.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Itinerary {

	private Integer id;
	private Date date;
	private Date hour;
	private List<PointOfInterest> selectedPois;
	private StartingPoint start;

	public Date getDate() {
		return date;
	}

	public void setDate(String date) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.date = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getID() {
		return String.valueOf(this.id);
	}

	public void setID(String id) {
		this.id = Integer.parseInt(id);
	}

	public Date getHour() {
		return hour;
	}

	public void setHour(String hour) {
		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");
		try {
			this.hour = sdf.parse(hour);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
