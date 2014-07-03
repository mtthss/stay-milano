package com.staymilano.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper mInstance;
	private Context context;

	private static final String DATABASE_NAME = "staymilano.db";
	private static final int DATABASE_VERSION = 13;


	// Lo statement SQL di creazione del database
	private static final String POINTOFINTEREST_CREATE = "create table "
			+ PointOfInterestDAO.TABELLA + "(" + PointOfInterestDAO.ID
			+ " integer primary key autoincrement, " + PointOfInterestDAO.NAME
			+ " text not null, " + PointOfInterestDAO.DESCRIPTION
			+ " text not null, " + PointOfInterestDAO.TYPE + " text not null, "
			+ PointOfInterestDAO.AREA + " text not null,"
			+ PointOfInterestDAO.LATITUDE + " text not null,"
			+ PointOfInterestDAO.LONGITUDE + " text not null);";

	private static final String ITINERARY_CREATE = "create table "
			+ ItineraryDAO.TABELLA + "(" + ItineraryDAO.ID
			+ " integer primary key autoincrement, " + ItineraryDAO.DATA
			+ " text not null);";

	private static final String SELECTED_POI = "create table "
			+ SelectedPOIDAO.TABELLA 
			+" (_id integer primary key autoincrement,"
			+SelectedPOIDAO.ITINERARY_ID+" integer , "
			+SelectedPOIDAO.POI_ID+" integer , "
			+SelectedPOIDAO.VISITED+" text);";

	private static final String AREA_CREATE = "create table " 
			+ AreaDAO.TABELLA
			+ "(" + AreaDAO.ID + " integer primary key autoincrement, "
			+ AreaDAO.NAME + " text not null, " 
			+ AreaDAO.LATITUDE + " text not null, "
			+ AreaDAO.LONGITUDE + " text not null);";
	
	private static final String CREATE_START_POINTS = "create table " 
			+ StartPointDAO.TABELLA
			+ "(_id integer primary key autoincrement,"
			+ StartPointDAO.START_LAT + " text not null, " 
			+ StartPointDAO.START_LONG + " text not null, "
			+ SelectedPOIDAO.ITINERARY_ID + " integer );";
	
	private static final String CREATE_BIKE_STATIONS = "create table " 
			+ BikeStationDAO.TABELLA
			+ "(_id integer primary key autoincrement,"
			+ BikeStationDAO.ITINERARY_ID + " text not null, "
			+ BikeStationDAO.STATION_NAME + " text not null, "
			+ BikeStationDAO.LAT + " text not null, " 
			+ BikeStationDAO.LONG + " text not null );";
			

	// Costruttore
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public static DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context.getApplicationContext());
		}
		return mInstance;
	}

	// Questo metodo viene chiamato durante la creazione del database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(POINTOFINTEREST_CREATE);
		database.execSQL(ITINERARY_CREATE);
		database.execSQL(AREA_CREATE);
		database.execSQL(CREATE_BIKE_STATIONS);
		database.execSQL(SELECTED_POI);
		database.execSQL(CREATE_START_POINTS);


		loadPOI(database);
		loadAREA(database);

	}

	// Questo metodo viene chiamato durante l'upgrade del database, ad esempio
	// Quando viene incrementato il numero di versione
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS pointofinterest");
		database.execSQL("DROP TABLE IF EXISTS area");
		database.execSQL(POINTOFINTEREST_CREATE);
		database.execSQL(AREA_CREATE);
		
		database.execSQL("DROP TABLE IF EXISTS itinerary");
		database.execSQL("DROP TABLE IF EXISTS selectedpoi");
		database.execSQL("DROP TABLE IF EXISTS bike_stations");
		database.execSQL(ITINERARY_CREATE);
		database.execSQL(SELECTED_POI);
		database.execSQL(CREATE_BIKE_STATIONS);
		
		database.execSQL("DROP TABLE IF EXISTS start_points");
		database.execSQL(CREATE_START_POINTS);
		
		loadPOI(database);
		loadAREA(database);
	}

	private void loadPOI(SQLiteDatabase database) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(context
					.getAssets().open("poi.csv"), "UTF-8"));
			int i = 0;
			String line = null;

			while ((line = reader.readLine()) != null) {
				PointOfInterestDAO.insertPOI(database, line);
				/*String insert_data = "INSERT INTO "
						+ PointOfInterestDAO.TABELLA + " VALUES(" + i + ","
						+ PointOfInterestDAO.getInsertQuery(line) + ")";
				i++;
				database.execSQL(insert_data);*/

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void loadAREA(SQLiteDatabase database) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(context
					.getAssets().open("areas_points.csv"), "UTF-8"));
			String line = null;


			while ((line = reader.readLine()) != null) {
				AreaDAO.insertArea(database, line);
				// database.insert(AreaDAO.TABELLA,
				// null,AreaDAO.getContentValues(line));
				/*String insert_data = "INSERT INTO " + AreaDAO.TABELLA + " VALUES(" + i + "," + AreaDAO.getInsertQuery(line)	+ ")";
				i++;
				database.execSQL(insert_data);*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}