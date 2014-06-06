package com.staymilano.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.staymilano.database.ItineraryDAO;
import com.staymilano.database.SelectedPOIDAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserInfo implements Serializable{

	private static final long serialVersionUID = 8354095811783356274L;
	
	private List<Itinerary> itineraries;
	private static UserInfo user;

	private UserInfo(SQLiteDatabase readableDatabase) {
		Cursor cur=ItineraryDAO.getAllItineraries(readableDatabase);
		int cont=cur.getCount();
		cur.moveToFirst();
		if(cont>0){
			do {
				Itinerary it = new Itinerary();
				String s = cur.getString(0);
				it.setID(s);
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				try {
					String s2=cur.getString(1);
					cal.setTime(sdf.parse(s2));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (cur.moveToNext());
		}
		
	}
	
	public static UserInfo getUserInfo(SQLiteDatabase readableDatabase) {
		if(user==null){
			user=new UserInfo(readableDatabase);
		}
		return user;
	}
	
	public void saveItinerary(Itinerary it, SQLiteDatabase db){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String stringDate = sdf.format(it.getDate().getTime());
		String id=ItineraryDAO.insertItinerary(db, stringDate);
		it.setID(id);
		for(PointOfInterest poi:it.getPois()){
			SelectedPOIDAO.insertItinerary(db, poi.getId(), it.getID());
		}
	}

	public List<Itinerary> getItineraries() {
		return itineraries;
	}

}
