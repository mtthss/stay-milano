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
	private List<BikeStation> selectedBikeSt=new ArrayList<BikeStation>();
	private StartingPoint start;
	
	
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
	
	public void setPois(List<PointOfInterest> selectedPOI){
		selectedPois.addAll(selectedPOI);
	}
	
	public List<PointOfInterest> getPois(){
		List<PointOfInterest> poisPlusStart = new ArrayList<PointOfInterest>();
		//TODO aggiungere start come primo poi, ah no problema perchè lo chiama anche prima di aver settato lo starting point
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
	
	public List<LatLng> getAllItineraryCoordinates(){
		List<LatLng> listLatLng = new ArrayList<LatLng>();
		listLatLng.add(start.getPosition());
		for(PointOfInterest p : selectedPois){
			listLatLng.add(p.getPosition());
		}
		for(BikeStation bikeSt : selectedBikeSt){
			listLatLng.add(bikeSt.getPosition());
		}
		return listLatLng;
	}
	
	public List<PointOfItinerary> getAllPointOfThisItinerary(){
		List<PointOfItinerary> pois=new ArrayList<PointOfItinerary>();
		pois.add(start);
		for(PointOfInterest p:selectedPois){
			pois.add(p);
		}
		for(BikeStation b:selectedBikeSt){
			pois.add(b);
		}
		return pois;
		
	}

	public StartingPoint getStartingPoint(){
		return this.start;
	}
	
	public void setStartingPoint(LatLng coord){
		start=new StartingPoint();
		start.setPosition(coord);
	}
	
	public List<BikeStation> getSelectedBikeSt() {
		return selectedBikeSt;
	}

	public void setSelectedBikeSt(List<BikeStation> selectedBikeSt) {
		this.selectedBikeSt = selectedBikeSt;
	}

	public BikeStation saveBikeStation(String title, LatLng position) {
		BikeStation bikest=new BikeStation();
		bikest.setName(title);
		bikest.setPosition(position);
		this.selectedBikeSt.add(bikest);
		return bikest;		
	}

	public boolean hasThisBikeStYet(String name) {
		for(BikeStation bikeSt:selectedBikeSt){
			if(bikeSt.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

}
