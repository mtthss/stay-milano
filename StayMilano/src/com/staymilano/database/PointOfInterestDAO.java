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
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	public static final String TABELLA = "pointofinterest";
	public static final String[] COLONNE = new String[] { ID, NAME,
			DESCRIPTION, TYPE, AREA, LATITUDE, LONGITUDE };

	public static Cursor getAllPOI(SQLiteDatabase db) {
		return db.query(TABELLA, COLONNE, null, null, null, null, null);
	}

	public static Cursor getPOIByArea(SQLiteDatabase db, String area)
			throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, AREA + "= '" + area + "'",null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public static Cursor getPOIById(SQLiteDatabase db, String id)
			throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, ID + "= '" + id + "'",null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public static Cursor getPOIByName(SQLiteDatabase db, String name)
			throws SQLException {
		Cursor c = db.query(true, TABELLA, COLONNE, NAME + "= '" + name + "'",null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public static long insertPOI(SQLiteDatabase db, String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
		String name = stringTokenizer.nextToken(), description = stringTokenizer
				.nextToken(), type = stringTokenizer.nextToken(), area = stringTokenizer
				.nextToken(), lat = stringTokenizer.nextToken(), lng = stringTokenizer
				.nextToken();
		ContentValues value = new ContentValues();
		value.put(NAME, name);
		value.put(DESCRIPTION, description);
		value.put(TYPE, type);
		value.put(AREA, area);
		value.put(LATITUDE, lat);
		value.put(LONGITUDE, lng);
		return db.insert(TABELLA, null, value);
	}
}
