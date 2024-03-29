package com.staymilano.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SelectedPOIDAO {

	public static final String ITINERARY_ID = "_itinerary_id";
	public static final String POI_ID = "poi_id";
	public static final String VISITED = "visited";
	
	public static final String TABELLA = "selectedpoi";
	public static final String[] COLONNE = new String[]{ITINERARY_ID, POI_ID, VISITED};
	
	
	public static void insertSelectedPOI(SQLiteDatabase db, String poi_id, String itinerary_id){
		ContentValues values=new ContentValues();
		values.put(ITINERARY_ID, itinerary_id);
		values.put(POI_ID, poi_id);
		values.put(VISITED, false);
		db.insert(TABELLA, null, values);

	}
	
	public static Cursor getSelectedPOIByPOIid(SQLiteDatabase db, String id) throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, POI_ID + "= '"+id+"'", null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
	    return c;
	}
	
	public static Cursor getSelectedPOIByItineraryId(SQLiteDatabase db, String id) throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, ITINERARY_ID + "='"+id+"'", null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
	    return c;
	}
	
    public static boolean deleteSelectedPOI(SQLiteDatabase db, String itinerary_id, String poi_id) {
        return db.delete(TABELLA, ITINERARY_ID + "=" + itinerary_id+" and "+
        						POI_ID + "=" + poi_id, null) > 0;
    }
    
    public static boolean deleteSelectedPOIByItinerary(SQLiteDatabase db, String itinerary_id) {
        return db.delete(TABELLA, ITINERARY_ID + "='" + itinerary_id+"'", null) > 0;
    }
}
