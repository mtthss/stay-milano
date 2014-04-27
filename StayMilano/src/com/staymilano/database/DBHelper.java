package com.staymilano.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "staymilano.db";
    private static final int DATABASE_VERSION = 1;

    // Lo statement SQL di creazione del database
    private static final String POINTOFINTEREST_CREATE = "create table "+ POIAdapter.TABELLA
    		+ "("+POIAdapter.ID+" integer primary key autoincrement,"
    				+ POIAdapter.NAME+" text not null, "
    						+ POIAdapter.DESCRIPTION+" text not null, "
    								+ POIAdapter.TYPE+" text not null, "
    										+ POIAdapter.AREA+" text not null);";
    
    private static final String ITINERARY_CREATE = "create table "+ItineraryAdapter.TABELLA
    		+ "("+ItineraryAdapter.ID+" integer primary key autoincrement, "
    				+ ItineraryAdapter.DATA+" text not null, "
    						+ ItineraryAdapter.START_TIME+" text not null);";
    
    private static final String SELECTED_POI = "create table selectedpoi "
    		+ "(_itinerary_id primary key, _poi_id, visited boolean);";

    // Costruttore
    public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Questo metodo viene chiamato durante la creazione del database
    @Override
    public void onCreate(SQLiteDatabase database) {
    	database.execSQL(POINTOFINTEREST_CREATE);
    	database.execSQL(ITINERARY_CREATE);
    	database.execSQL(SELECTED_POI);
    	File file = new File("assets/poi.csv");
    	try {
			FileInputStream is = new FileInputStream(file);
			load(database,new InputStreamReader(is));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

    }

	// Questo metodo viene chiamato durante l'upgrade del database, ad esempio quando viene incrementato il numero di versione
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    	database.execSQL("DROP TABLE IF EXISTS pointofinterest");
    	database.execSQL(POINTOFINTEREST_CREATE);
    	load(database,new InputStreamReader(System.in));             
    }  
    
	private void load(SQLiteDatabase database, InputStreamReader inputStreamReader) {
		BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
                String line = null;
                while ( (line = reader.readLine()) != null ) {
                        database.insert(POIAdapter.TABELLA, null, POIAdapter.getContentValues(line));
                } 
                reader.close();
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
                if (inputStreamReader != null) {
                        try {
                                inputStreamReader.close();
                        } catch (IOException e) {
                        }
                }
        }
		
	}

}
