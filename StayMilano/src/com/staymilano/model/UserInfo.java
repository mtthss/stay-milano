package com.staymilano.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.staymilano.database.ItineraryDAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserInfo implements Serializable{

	private static final long serialVersionUID = 8354095811783356274L;
	
	private List<Itinerary> itineraries;

	public void setItineraries(SQLiteDatabase db) {
		itineraries=new ArrayList<Itinerary>();
		Cursor cursor=ItineraryDAO.getAllItinerary(db);
		while(cursor.moveToNext()){
			Itinerary it= new Itinerary();
			it.setID(cursor.getString(0));
			it.setDate(cursor.getString(1));
			it.setHour(cursor.getString(2));
			itineraries.add(it);
		}
	}

	public List<Itinerary> getItineraries() {
		return itineraries;
	}
	
}
