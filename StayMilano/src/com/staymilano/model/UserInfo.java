package com.staymilano.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.staymilano.database.ItineraryDAO;
import com.staymilano.database.PointOfInterestDAO;
import com.staymilano.database.SelectedPOIDAO;
import com.staymilano.database.StartPointDAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserInfo implements Serializable{

	private static final long serialVersionUID = 8354095811783356274L;
	private List<Itinerary> itineraries;
	private static UserInfo user;

	
	private UserInfo(SQLiteDatabase readableDatabase) {
		
		Cursor cur = ItineraryDAO.getAllItineraries(readableDatabase);
		int cont = cur.getCount();
		cur.moveToFirst();
		if (cont > 0) {
			itineraries = new ArrayList<Itinerary>();
			do {
				Itinerary it = new Itinerary(cur.getString(0),cur.getString(1));				
				fillItinerary(it,readableDatabase);
				itineraries.add(it);
			} while (cur.moveToNext());
			orderItByDate();
		}

	}
	
	private void orderItByDate() {
		/*
		for(int i=1;i<itineraries.size();i++){
			Calendar c = itineraries.get(i).getDate();
			
			int j;
			for(j=i-1; j>=0 && temp< )
			
		}
		*/
	}

	private void fillItinerary(Itinerary it,SQLiteDatabase readableDatabase) {
		
		List<PointOfInterest> pois= new ArrayList<PointOfInterest>();
		Cursor cur = SelectedPOIDAO.getSelectedPOIByItineraryId(readableDatabase, it.getID());
		if (cur.getCount() > 0) {
			do {
				Cursor c= PointOfInterestDAO.getPOIById(readableDatabase, cur.getString(1));
				if (cur.getCount() > 0){
					PointOfInterest pointOfInterest=new PointOfInterest(c);
					pois.add(pointOfInterest);
				}
			} while (cur.moveToNext());
		}
		if(pois.size()>0){
			it.setPois(pois);
			//TODO passo2
			Cursor c_start = StartPointDAO.getStartPointById(readableDatabase, it.getID());
			if(c_start.getCount()>0){
				c_start.moveToFirst();
				String lat = c_start.getString(1);
				String lon = c_start.getString(2);
				LatLng startCoord = new LatLng(Double.valueOf(lat),Double.valueOf(lon));
				it.setStart(startCoord);
			}
		}	
	}

	public static UserInfo getUserInfo(SQLiteDatabase readableDatabase) {
		
		if(user==null){
			user=new UserInfo(readableDatabase);
		}
		return user;
	}

	public static UserInfo getRefreshedUserInfo(SQLiteDatabase readableDatabase) {
		
		user=new UserInfo(readableDatabase);
		return user;
	}
	
	public String saveItinerary(Itinerary it, SQLiteDatabase db){
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String stringDate = sdf.format(it.getDate().getTime());
		String id=ItineraryDAO.insertItinerary(db, stringDate);
		it.setID(id);
		for(PointOfInterest poi:it.getPois()){
			SelectedPOIDAO.insertItinerary(db, poi.getId(), it.getID());
		}
		return id;
	}

	public List<Itinerary> getItineraries() {
		
		return itineraries;
	}

	public Itinerary getItinerary(String itinerary_id) {
		
		Itinerary result = new Itinerary();
		for(Itinerary it:itineraries){
			if(itinerary_id.equals(it.getID())){
				result=it;
			}
		}
		return result;
	}
	
	public Itinerary getItineraryByDate(Date d){
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String dateToFind = sdf.format(d);
		
		for(Itinerary it : itineraries){
			if(sdf.format(it.getDate()).equals(dateToFind)){
				return it;
			}
		}
		
		return null;
	}

}
