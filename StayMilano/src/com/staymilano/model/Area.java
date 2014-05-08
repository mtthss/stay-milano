package com.staymilano.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.staymilano.database.PointOfInterestDAO;

public class Area implements Serializable{


	private static final long serialVersionUID = 3577242940737875282L;
	private String name;
	private List<PointOfInterest> pois;

	public Area(SQLiteDatabase db, AreasName nm) {
		this.name=nm.toString();
		this.pois=fillPois(db,nm);
	}
	
	private List<PointOfInterest> fillPois(SQLiteDatabase db, AreasName nm) {
		pois=new ArrayList<PointOfInterest>();
		Cursor cur= PointOfInterestDAO.getPOIByArea(db, nm.toString().toLowerCase());
		while(cur.moveToNext()){
			PointOfInterest poi=new PointOfInterest();
			poi.setName(cur.getString(1));
			poi.setDescription(cur.getString(2));
			poi.setType(cur.getString(3));
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public List<PointOfInterest> getPois() {
		return pois;
	}

}
