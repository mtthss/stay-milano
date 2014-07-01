package com.staymilano.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BikeStationDAO {
	
	public static final String ITINERARY_ID = "_itinerary_id";
	public static final String STATION_NAME = "name";
	public static final String START_LAT = "start_lat";
	public static final String START_LONG = "start_long";
	
	public static final String TABELLA = "bike_stations";
	public static final String COLONNE[]=new String[]{ITINERARY_ID,STATION_NAME,START_LAT,START_LONG};
	
	public static void insertBikeStation(SQLiteDatabase db, String itinerary_id, String station_name, String startLat,
			String startLong) {
		
		String s = station_name.replace("-", "");

		String insert_data = "INSERT INTO " + TABELLA + " VALUES ('"
				+ itinerary_id + "','"
				+ s + "','"
				+ startLat+ "','"
				+ startLong + "')";
		db.execSQL(insert_data);
	}
	
	public static Cursor getBikeStationById(SQLiteDatabase db, String id) throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, ITINERARY_ID + "= '" + id + "'", null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
	    return c;
	}
	
	 public static boolean deleteStartPointPOI(SQLiteDatabase db, String itinerary_id, String station_name) {
	        return db.delete(TABELLA, ITINERARY_ID + "='" + itinerary_id+"' and "+STATION_NAME+"='"+station_name+"'", null) > 0;
	    }

}
