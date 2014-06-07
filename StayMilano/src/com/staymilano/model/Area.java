package com.staymilano.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.staymilano.database.AreaDAO;
import com.staymilano.database.PointOfInterestDAO;

public class Area implements Serializable {

	private static final long serialVersionUID = 3577242940737875282L;

	private String name;
	private List<PointOfInterest> pois;
	private PolygonOptions pol;
	private LatLng center;

	public Area(SQLiteDatabase db, AreasName nm) {
		this.name = nm.toString().toLowerCase();
		pois = new ArrayList<PointOfInterest>();
		this.pois = fillPois(db);
		createPolygon(db);
		//setCenter(db);
	}

	/*private void setCenter(SQLiteDatabase db) {
		Cursor cur=AreaDAO.getCenterByArea(db, name);
		if(cur.getCount()!=0){
			Double d1=Double.valueOf(cur.getString(2));
			Double d2=Double.valueOf(cur.getString(3));
			center=new LatLng(d1,d2);
		}
	}*/

	private void createPolygon(SQLiteDatabase db) {
		pol = new PolygonOptions();
		Cursor cur = AreaDAO.getPointsByArea(db, name);
		do{
			Double d1=Double.valueOf(cur.getString(2));
			Double d2=Double.valueOf(cur.getString(3));
			
			if (cur.getString(4).equalsIgnoreCase("false")) {
				LatLng latlng = new LatLng(d1, d2);
				pol.add(latlng);
			} else {
				center = new LatLng(d1, d2);
			}
		}while (cur.moveToNext());
	}

	private List<PointOfInterest> fillPois(SQLiteDatabase db) {
		Cursor cur = PointOfInterestDAO.getPOIByArea(db, name);
		do{
			PointOfInterest poi = new PointOfInterest(cur);
			pois.add(poi);
		}while (cur.moveToNext());
		return pois;
	}

	public String getName() {
		return name;
	}

	public List<PointOfInterest> getPois() {
		return pois;
	}

	public PolygonOptions getPolygon() {
		return pol;
	}
	
	public LatLng getCenter(){
		return center;
	}

}
