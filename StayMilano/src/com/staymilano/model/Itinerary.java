package com.staymilano.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Itinerary implements Serializable{

	private static final long serialVersionUID = -9024000010811787150L;
	
	private Integer id;
	private Calendar date;
	private List<PointOfInterest> selectedPois=new ArrayList<PointOfInterest>();
	private LatLng start;
	
	
	public Itinerary(String id,String date){
		this.id=Integer.parseInt(id);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			cal.setTime(sdf.parse(date));
			this.date=cal;
		} catch (ParseException e) {
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
		List<PointOfInterest> poisPlusStart = new ArrayList<PointOfInterest>();
		//TODO aggiungere start come primo poi, ah no problema perch� lo chiama anche prima di aver settato lo starting point
		//poisPlusStart.add(start);
		for(PointOfInterest p : selectedPois){
			poisPlusStart.add(p);
		}
		return poisPlusStart;
	}

	public String getID() {
		return String.valueOf(this.id);
	}

	public void setID(String id) {
		this.id = Integer.parseInt(id);
	}
	
	public static List<LatLng> coordinatesOfPoiList(List<PointOfInterest> points){
		
		List<LatLng> listLatLng = new ArrayList<LatLng>();
		
		for(PointOfInterest p : points){
			listLatLng.add(p.getPosition());
		}
		
		return listLatLng;
	}

	public void setStart(LatLng startCoord) {
		
		//TODO passo 3
		start = startCoord;
	}

	public LatLng getStart() {
		
		// TODO passo 4
		return start;
	}

}
