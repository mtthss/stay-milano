package com.staymilano.database;

import java.util.StringTokenizer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PointOfInterestDAO {
	
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String AREA = "area";
	public static final String LATITUDE= "latitude";
	public static final String LONGITUDE= "longitude";
	    
	public static final String TABELLA = "pointofinterest";
	public static final String[] COLONNE = new String[]{ID, NAME, DESCRIPTION, TYPE, AREA};
	    
	public static ContentValues getContentValues(String line) {
		 ContentValues result = new ContentValues();
	        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
	        result.put(NAME, stringTokenizer.nextToken());
	        result.put(DESCRIPTION, stringTokenizer.nextToken());
	        result.put(TYPE, stringTokenizer.nextToken());
	        result.put(AREA, stringTokenizer.nextToken());
	        result.put(LATITUDE, stringTokenizer.nextToken());
	        result.put(LONGITUDE, stringTokenizer.nextToken());
	        return result;
	}

	public static Cursor getAllPOI(SQLiteDatabase db){
        return db.query(TABELLA, COLONNE, null, null, null, null, null);
    }
	
	public static Cursor getPOIByArea(SQLiteDatabase db, String area) throws SQLException {
        Cursor c = db.query(true, TABELLA, COLONNE, AREA + "=" + "'"+area+"'", null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
	
	public static Cursor getPOIById(SQLiteDatabase db, String id) throws SQLException {
        Cursor c = db.query(true, TABELLA, COLONNE, ID + "=" + "'"+id+"'", null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
}
