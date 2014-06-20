package com.staymilano.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StartPointDAO {
	
	public static final String ITINERARY_ID = "_itinerary_id";
	public static final String START_LAT = "start_lat";
	public static final String START_LONG = "start_long";
	
	public static final String TABELLA = "start_points";
	public static final String[] COLONNE = new String[]{ITINERARY_ID, START_LAT, START_LONG};
	
	
	
	public static void insertStartingPoint(SQLiteDatabase db, String itinerary_id, String startLat, String startLong){
		
		String insert_data = "INSERT INTO " + TABELLA
				+ " VALUES ('" + itinerary_id+ "', '" + startLat+ "', '" + startLong + "')";
		db.execSQL(insert_data);
	}

	public static void updateItineraryStartPoint(SQLiteDatabase db, String itinerary_id, String startLat, String startLong){
		
		ContentValues v = new ContentValues();
		v.put(ITINERARY_ID, itinerary_id);
		v.put(START_LAT, startLat);
		v.put(START_LONG, startLong);
		
		db.update(TABELLA, v, ITINERARY_ID + "='" + itinerary_id + "'", null);
	}
	
	public static Cursor getStartPointById(SQLiteDatabase db, String id) throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, ITINERARY_ID + "= '" + id + "'", null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
	    return c;
	}
	
    public static boolean deleteStartPointPOI(SQLiteDatabase db, String itinerary_id, String poi_id) {
        return db.delete(TABELLA, ITINERARY_ID + "=" + itinerary_id + " and "+
        						ITINERARY_ID + "=" + poi_id, null) > 0;
    }
	
}