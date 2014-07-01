package com.staymilano.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.internal.cb;
import com.google.android.gms.maps.model.LatLng;
import com.staymilano.database.BikeStationDAO;
import com.staymilano.database.ItineraryDAO;
import com.staymilano.database.PointOfInterestDAO;
import com.staymilano.database.SelectedPOIDAO;
import com.staymilano.database.StartPointDAO;

public class UserInfo implements Serializable{

	private static final long serialVersionUID = 8354095811783356274L;
	private List<Itinerary> itineraries;
	private static boolean updated;
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

	private void fillItinerary(Itinerary it, SQLiteDatabase readableDatabase) {

		List<PointOfInterest> pois = new ArrayList<PointOfInterest>();
		Cursor cur = SelectedPOIDAO.getSelectedPOIByItineraryId(
				readableDatabase, it.getID());
		if (cur.getCount() > 0) {
			do {
				Cursor c = PointOfInterestDAO.getPOIById(readableDatabase,
						cur.getString(1));
				if (cur.getCount() > 0) {
					PointOfInterest pointOfInterest = new PointOfInterest(c);
					pois.add(pointOfInterest);
				}
			} while (cur.moveToNext());
		}
		if (pois.size() > 0) {
			it.setPois(pois);
			Cursor c_start = StartPointDAO.getStartPointById(readableDatabase,
					it.getID());
			if (c_start.getCount() > 0) {
				c_start.moveToFirst();
				String lat = c_start.getString(1);
				String lon = c_start.getString(2);
				LatLng startCoord = new LatLng(Double.valueOf(lat),
						Double.valueOf(lon));
				it.setStartingPoint(startCoord);
			}
			Cursor c_bike = BikeStationDAO.getBikeStationById(readableDatabase,
					it.getID());
			c_bike.moveToFirst();
			if (c_bike.getCount() > 0) {
				List<BikeStation> bikes = new ArrayList<BikeStation>();
				do{
					BikeStation bike = new BikeStation();
					int i=c_start.getColumnIndex(BikeStationDAO.STATION_NAME);
					bike.setName("Bike Station");
					String lat = c_start.getString(c_start.getColumnIndex(BikeStationDAO.START_LAT));
					String lon = c_start.getString(c_start.getColumnIndex(BikeStationDAO.START_LONG));
					LatLng bikeCoord = new LatLng(Double.valueOf(lat),
							Double.valueOf(lon));
					bike.setPosition(bikeCoord);
					bikes.add(bike);
				}while(c_bike.moveToNext());
				it.setSelectedBikeSt(bikes);
			}
		}
	}

	public static UserInfo getUserInfo(SQLiteDatabase readableDatabase) {
		
		if(user==null||isUpdated()){
			user=new UserInfo(readableDatabase);
			UserInfo.updated=false;
		}
		return user;
	}
	
	public String saveItinerary(Itinerary it, SQLiteDatabase db){
		UserInfo.updated=true;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String stringDate = sdf.format(it.getDate().getTime());
		String id=ItineraryDAO.insertItinerary(db, stringDate);
		it.setID(id);
		for(PointOfItinerary poi:it.getPois()){
			SelectedPOIDAO.insertItinerary(db, ((PointOfInterest)poi).getId(), it.getID());
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

	public String updateItinerary(Itinerary it, SQLiteDatabase db) {
		UserInfo.updated=true;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String stringDate = sdf.format(it.getDate().getTime());
		if(ItineraryDAO.updateItinerary(db, it.getID(), stringDate)){
			SelectedPOIDAO.deleteSelectedPOIByItinerary(db, it.getID());
			for(PointOfInterest poi:it.getPois()){
				SelectedPOIDAO.insertItinerary(db, poi.getId(), it.getID());
			}
		}
		return null;
	}

	public static boolean isUpdated() {
		return updated;
	}
	
	public static String itineraryToString(Itinerary it){
		//TODO riguardare
		/* Crea Stringa:
		 * data; starting point; nomePoi1, nomePoi2, ... ; idStallo1, lat1, long1, idStallo2, lat2, long2, ... ;  */
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String stringDate = sdf.format(it.getDate().getTime());
		
		String s = stringDate+";";
		s = s+it.getStartingPoint().getPosition().latitude+","+it.getStartingPoint().getPosition().longitude+";";
		
		List<PointOfInterest> stalli = new ArrayList<PointOfInterest>();
		int length = it.getPois().size();
		
		for(int i=0; i<length;i++){
			PointOfInterest poi = it.getPois().get(i);
			if(poi.getType()!="stallo"){
				s = s+poi.getName();
				if(i!=length-1){
					s = s+",";
				}
			}else{
				stalli.add(poi);				
			}
		}
		
		for(int j=0; j<stalli.size(); j++){
			s = s+stalli.get(j).getDescription()+","+stalli.get(j).getPosition().latitude+","+stalli.get(j).getPosition().longitude;
		}
		
		return s;
	}
	
	public static Itinerary saveStringToItinerary(String s){
		//TODO FINIRE
		Itinerary it = null;
		
		
		return it;
	}

	public static BikeStation saveBikeStation(SQLiteDatabase db, Itinerary it, String title, LatLng position) {
		UserInfo.updated=true;
		BikeStationDAO.insertBikeStation(db, it.getID(), title, ((Double)position.latitude).toString(), ((Double)position.longitude).toString());
		return it.saveBikeStation(title, position);
	}

}
