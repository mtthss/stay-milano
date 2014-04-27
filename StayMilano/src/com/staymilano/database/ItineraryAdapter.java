package com.staymilano.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItineraryAdapter {
	
	public static final String ID = "_id";
	public static final String DATA = "data";
	public static final String START_TIME = "starttime";
	
	public static final String TABELLA = "itinerary";
	public static final String[] COLONNE = new String[]{ID, DATA, START_TIME};
	
	public static void insertItinerary(SQLiteDatabase db, String data, String start){
		ContentValues v = new ContentValues();
		v.put(DATA, data);
		v.put(START_TIME, start);
		
		db.insert(TABELLA, null, v);
	}
	
	public static Cursor getAllItinerary(SQLiteDatabase db){
        return db.query(TABELLA, COLONNE, null, null, null, null, null);
    }
	
	public static Cursor getItineraryByDate(SQLiteDatabase db, String date) throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, DATA + "=" + date, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
	    return c;
	}
	
	public static boolean updateItinerary(SQLiteDatabase db, long id, String data, String starttime){
        ContentValues v = new ContentValues();
        v.put(DATA, data);
        v.put(START_TIME, starttime);
 
        return db.update(TABELLA, v, ID + "=" + id, null) >0; 
    }
	
    public static boolean deleteItinerary(SQLiteDatabase db, String id) {
    	SelectedPOIAdapter.deleteSelectedPOIByItinerary(db, id);
        return db.delete(TABELLA, ID + "=" + id, null) > 0;
    }

}
