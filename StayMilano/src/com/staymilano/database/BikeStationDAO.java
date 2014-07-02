package com.staymilano.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BikeStationDAO {
	
	public static final String ITINERARY_ID = "_id";
	public static final String STATION_NAME = "name";
	public static final String LAT = "lat";
	public static final String LONG = "lng";

	
	public static final String TABELLA = "bike_stations";
	public static final String[] COLONNE=new String[]{ITINERARY_ID,STATION_NAME,LAT,LONG};
	
	
	public static Cursor getBikeStationById(SQLiteDatabase db, String id) {
		Cursor c = db.query(true, TABELLA, COLONNE, ITINERARY_ID + "=" +"'"+id+"'", null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
	    return c;
	}


	public static void insertBikeStation(SQLiteDatabase db, String id,
			String name, String lat, String lng) {
		String insert_data = "INSERT INTO " + TABELLA+ " VALUES(" + id + ",'" + name+ "','" + lat+ "','" + lng+ "')";
		db.execSQL(insert_data);
	}


	public static boolean deleteBikeStations(SQLiteDatabase db, String id) {
        return db.delete(TABELLA, ITINERARY_ID + "=" + "'"+id+"'", null) > 0;
		
	}
}
