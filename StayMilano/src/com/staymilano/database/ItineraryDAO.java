package com.staymilano.database;

import com.google.android.gms.maps.model.LatLng;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItineraryDAO {
	
	public static final String ID = "_id";
	public static final String DATA = "data";
	
	public static final String TABELLA = "itinerary";
	public static final String[] COLONNE = new String[]{ID, DATA};
	
	public static long insertItinerary(SQLiteDatabase db, String data){
		ContentValues values=new ContentValues();
		values.put(DATA	, data);
		return db.insert(TABELLA, null, values);

		/*String insert_data = "INSERT INTO " + TABELLA
				+ " VALUES('" + data+ "')";
		db.execSQL(insert_data);
		return String.valueOf(i);*/
	}
	
	public static Cursor getAllItineraries(SQLiteDatabase db){
        return db.query(TABELLA, COLONNE, null, null, null, null, null);
    }
	
	public static Cursor getItineraryByDate(SQLiteDatabase db, String date) throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, DATA + "=" +"'"+date+"'", null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
	    return c;
	}
	
	public static Cursor getItineraryById(SQLiteDatabase db, String id) throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, ID + "=" +"'"+id+"'", null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
	    return c;
	}
	
	public static boolean updateItinerary(SQLiteDatabase db, String id, String data){
        ContentValues v = new ContentValues();
        v.put(DATA, data);
 
        return db.update(TABELLA, v, ID + "=" + "'"+id+"'", null) >0; 
    }
	
    public static boolean deleteItinerary(SQLiteDatabase db, String id) {
    	SelectedPOIDAO.deleteSelectedPOIByItinerary(db, id);
    	StartPointDAO.deleteStartPointPOI(db, id);
    	BikeStationDAO.deleteBikeStations(db, id);
        return db.delete(TABELLA, ID + "=" + "'"+id+"'", null) > 0;
    }
    
}


